package com.payments.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.payments.model.Branch;
import com.payments.model.BranchConnection;
import com.payments.service.BranchService;

import java.util.List;

@ActiveProfiles("com")
@AutoConfigureJsonTesters
@AutoConfigureMockMvc
@WebMvcTest(BranchController.class)
class BranchControllerTest {

    @MockitoBean BranchService branchService;

    @Autowired private MockMvc mockMvc;

    @Autowired JacksonTester<List<Branch>> branchesJacksonTester;

    @Autowired JacksonTester<List<BranchConnection>> branchConnectionsJacksonTester;


    @Test
    void testGetAllBranches() throws Exception {
        final var expectedBranches = branchesJacksonTester.readObject("getAllBranchesResponse.json");

        when(branchService.getAllBranches()).thenReturn(expectedBranches);

        final var mvcResult = mockMvc.perform(get("/api/v1/branches")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        final var jsonResponse = mvcResult.getResponse().getContentAsString();
        final var actualBranches = branchesJacksonTester.parseObject(jsonResponse);

        assertThat(actualBranches).isEqualTo(expectedBranches);
    }

    @Test
    void testGetAllBranchesWhenDbIsEmpty() throws Exception {
        mockMvc.perform(get("/api/v1/branches"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void testGetAllBranchConnections() throws Exception {
        final var expectedBranchConnections =
                branchConnectionsJacksonTester.readObject("getAllBranchConnectionsResponse.json");

        when(branchService.getAllBranchConnections()).thenReturn(expectedBranchConnections);

        final var mvcResult = mockMvc.perform(get("/api/v1/branches/connections")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        final var jsonResponse = mvcResult.getResponse().getContentAsString();
        final var actualBranchConnections = branchConnectionsJacksonTester.parseObject(jsonResponse);

        assertThat(actualBranchConnections).isEqualTo(expectedBranchConnections);
    }
}
