package example.users;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.time.LocalDate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UsersApplicationTests {
	@Autowired
    TestRestTemplate restTemplate;

	@Test
	void shouldReturnUser() {
		ResponseEntity<String> response = restTemplate.getForEntity("/users/99", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		DocumentContext json = JsonPath.parse(response.getBody());
		assertThat(json.read("$.id", Long.class)).isEqualTo(99);
		assertThat(json.read("$.name", String.class)).isEqualTo("john");
		assertThat(json.read("$.lastName", String.class)).isEqualTo("doe");
		assertThat(json.read("$.email", String.class)).isEqualTo("john.doe@email.com");
		assertThat(json.read("$.birthday", String.class)).isEqualTo("2000-12-01");
		assertThat(json.read("$.phoneNumber", String.class)).isEqualTo("123456789");
		assertThat(json.read("$.address", String.class)).isEqualTo("1234 Main St");
	
	}
	@Test
	void shouldReturnUserNotFound() {
		ResponseEntity<String> response = restTemplate.getForEntity("/users/1000", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
		assertThat(response.getBody()).isBlank();
	}

	@Test
	void shouldReturnAllUsersWhenListIsRequested(){
		ResponseEntity<String> response = restTemplate.getForEntity("/users", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext json = JsonPath.parse(response.getBody());

		int userCount = json.read("$.length()", Integer.class);
		assertThat(userCount).isEqualTo(6);

		JSONArray ids = json.read("$..id");
		assertThat(ids).containsExactlyInAnyOrder(99, 100, 101, 102, 103, 104);
		
		JSONArray names = json.read("$..name");
		assertThat(names).containsExactlyInAnyOrder("john", "jane", "alex", "sarah", "michael", "emily");

		JSONArray lastNames = json.read("$..lastName");
		assertThat(lastNames).containsExactlyInAnyOrder("doe", "smith", "brown", "johnson", "wilson", "thompson");

		JSONArray emails = json.read("$..email");
		assertThat(emails).containsExactlyInAnyOrder("john.doe@email.com", "jane.smith@email.com", "alex.brown@email.com", "sarah.johnson@email.com", "michael.wilson@email.com", "emily.thompson@email.com");

		JSONArray birthdays = json.read("$..birthday");
		assertThat(birthdays).containsExactlyInAnyOrder("2000-12-01", "1995-05-10", "1988-09-15", "1992-03-25", "1980-11-05", "1998-07-20");

		JSONArray phoneNumbers = json.read("$..phoneNumber");
		assertThat(phoneNumbers).containsExactlyInAnyOrder("123456789", "987654321", "555555555", "111111111", "999999999", "444444444");

		JSONArray addresses = json.read("$..address");
		assertThat(addresses).containsExactlyInAnyOrder("1234 Main St", "456 Elm St", "789 Oak St", "321 Pine St", "654 Cedar St", "987 Birch St");
	}
	@Test
	@DirtiesContext
	void shouldCreateANewUser() {
		User user = new User(null, "john", "doe", "john.d@email.com", LocalDate.parse("2001-12-01"), "123456789", "1234 Main St");
		ResponseEntity<Void> createResponse = restTemplate.postForEntity("/users", user, Void.class);
		assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

		URI locationOfNewUser = createResponse.getHeaders().getLocation();
		ResponseEntity<String> response = restTemplate.getForEntity(locationOfNewUser, String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext json = JsonPath.parse(response.getBody());
		assertThat(json.read("$.id", Long.class)).isNotNull();
		assertThat(json.read("$.name", String.class)).isEqualTo("john");
		assertThat(json.read("$.lastName", String.class)).isEqualTo("doe");
		assertThat(json.read("$.email", String.class)).isEqualTo("john.d@email.com");
		assertThat(json.read("$.birthday", String.class)).isEqualTo("2001-12-01");
		assertThat(json.read("$.phoneNumber", String.class)).isEqualTo("123456789");
		assertThat(json.read("$.address", String.class)).isEqualTo("1234 Main St");
	}

	@Test 
	void shouldReturnAPageofUsers(){
		ResponseEntity<String> response = restTemplate.getForEntity("/users?page=0&size=2", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext json = JsonPath.parse(response.getBody());

		JSONArray page = json.read("$[*]");
		assertThat(page.size()).isEqualTo(2);

		
	}

	@Test
	void shouldReturnASortedPageOfUsers(){
		ResponseEntity<String> response = restTemplate.getForEntity("/users?page=0&size=2&sort=name,asc", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext json = JsonPath.parse(response.getBody());

		JSONArray page = json.read("$[*]");
		assertThat(page.size()).isEqualTo(2);

		assertThat(json.read("$[0].name", String.class)).isEqualTo("alex");
		assertThat(json.read("$[1].name", String.class)).isEqualTo("emily");
	}

	@Test
	void shouldReturnASortedPageOfUsersNoParametersAndUseDefaultValues(){
		ResponseEntity<String> response = restTemplate.getForEntity("/users", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext json = JsonPath.parse(response.getBody());

		JSONArray page = json.read("$[*]");
		assertThat(page.size()).isEqualTo(6);

		assertThat(json.read("$[0].lastName", String.class)).isEqualTo("brown");
		assertThat(json.read("$[1].lastName", String.class)).isEqualTo("thompson");

		assertThat(json.read("$[0].name", String.class)).isEqualTo("alex");
		assertThat(json.read("$[1].name", String.class)).isEqualTo("emily");
	}
	

}
