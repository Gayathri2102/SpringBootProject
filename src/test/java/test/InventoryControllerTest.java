package test;

import com.fasterxml.jackson.databind.ObjectMapper;
import treez.Main;
import treez.controller.InventoryController;
import treez.model.Inventory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Random;
import java.util.regex.Pattern;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Main.class)
@ActiveProfiles("test")
public class InventoryControllerTest {

    @InjectMocks
    InventoryController controller;

    @Autowired
    WebApplicationContext context;

    private MockMvc mvc;

    @Before
    public void initTests() {
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void ReadAllTest() throws Exception {
        mvc.perform(get("/inventories")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void CreateTest() throws Exception {
        Inventory r1 = mockInventory(0);
        byte[] r1Json = toJson(r1);

        //CREATE
        mvc.perform(post("/inventories")
                .content(r1Json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void UpdateTest() throws Exception {
        Inventory r1 = mockInventory(0);
        byte[] r1Json = toJson(r1);
        //UPDATE
        MvcResult result = mvc.perform(put("/inventories/0")
                .content(r1Json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void ReadTest() throws Exception {
    	mvc.perform(get("/inventories/0")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void DeleteTest() throws Exception {
        mvc.perform(delete("/inventories/0"))
                .andExpect(status().isOk());
    }

    private Inventory mockInventory(Integer prodId) {
    	Inventory r = new Inventory();
        r.setProdId(prodId);
        r.setDescription(prodId + "_description");
        r.setName(prodId + "_name");
        r.setPrice(prodId);
        r.setQuantity(prodId);
        return r;
    }

    private byte[] toJson(Object r) throws Exception {
        ObjectMapper map = new ObjectMapper();
        return map.writeValueAsString(r).getBytes();
    }
}