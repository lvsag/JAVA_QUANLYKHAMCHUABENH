package com.java.BaoCaoDoAn;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import com.java.BaoCaoDoAn.Model.TaiKhoan;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BaoCaoDoAnApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void contextLoads() {
	}

	@Test
	void adminBacSiPageRenders() throws Exception {
		TaiKhoan admin = new TaiKhoan();
		admin.setHoTen("Admin");
		admin.setEmail("admin@phongkham.vn");

		MockHttpSession session = new MockHttpSession();
		session.setAttribute("loggedInUser", admin);
		session.setAttribute("role", "ADMIN");

		mockMvc.perform(get("/admin/bac-si").session(session))
				.andExpect(status().isOk());
	}

}
