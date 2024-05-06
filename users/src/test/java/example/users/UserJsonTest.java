package example.users;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class UserJsonTest {
    @Autowired
    private JacksonTester<User> json;

    @Autowired
    private JacksonTester<User[]> jsonList;

    private User[] users;

    @BeforeEach
    void setUp(){
        users = Arrays.array(
            new User(99L, "john", "doe", "john.doe@email.com", LocalDate.parse("2000-12-01"), "123456789", "1234 Main St", "admin"),
            new User(100L, "jane", "smith", "jane.smith@email.com", LocalDate.parse("1995-05-10"), "987654321", "456 Elm St", "admin"),
            new User(101L, "alex", "brown", "alex.brown@email.com", LocalDate.parse("1988-09-15"), "555555555", "789 Oak St", "admin"),
            new User(102L, "sarah", "johnson", "sarah.johnson@email.com", LocalDate.parse("1992-03-25"), "111111111", "321 Pine St", "admin"),
            new User(103L, "michael", "wilson", "michael.wilson@email.com", LocalDate.parse("1980-11-05"), "999999999", "654 Cedar St", "admin"),
            new User(104L, "emily", "thompson", "emily.thompson@email.com", LocalDate.parse("1998-07-20"), "444444444", "987 Birch St", "admin")

        );
    }

    @Test
    public void userSerializationTest() throws IOException {
        User user = users[0];
        // Assert against a `.json` file in the same package as the test
        
        assertThat(this.json.write(user)).isStrictlyEqualToJson("single.json");
        

        assertThat(this.json.write(user)).hasJsonPathNumberValue("@.id");
        assertThat(this.json.write(user)).extractingJsonPathNumberValue("@.id").isEqualTo(99);

        assertThat(this.json.write(user)).hasJsonPathStringValue("@.name");
        assertThat(this.json.write(user)).extractingJsonPathStringValue("@.name").isEqualTo("john");

        assertThat(this.json.write(user)).hasJsonPathStringValue("@.lastName");
        assertThat(this.json.write(user)).extractingJsonPathStringValue("@.lastName").isEqualTo("doe");

        assertThat(this.json.write(user)).hasJsonPathValue("@.email");
        assertThat(this.json.write(user)).extractingJsonPathStringValue("@.email").isEqualTo("john.doe@email.com");

        assertThat(this.json.write(user)).hasJsonPathValue("@.phoneNumber");
        assertThat(this.json.write(user)).extractingJsonPathStringValue("@.phoneNumber").isEqualTo("123456789");

        assertThat(this.json.write(user)).hasJsonPathValue("@.address");
        assertThat(this.json.write(user)).extractingJsonPathStringValue("@.address").isEqualTo("1234 Main St");

        assertThat(this.json.write(user)).hasJsonPathValue("@.birthday");
        assertThat(this.json.write(user)).extractingJsonPathStringValue("@.birthday").isEqualTo("2000-12-01");

        assertThat(this.json.write(user)).hasJsonPathValue("@.owner");
        assertThat(this.json.write(user)).extractingJsonPathStringValue("@.owner").isEqualTo("admin");

        
    }

    @Test
    public void userListSerializationTest() throws IOException {
        // Assert against a `.json` file in the same package as the test
        assertThat(this.jsonList.write(users)).isStrictlyEqualToJson("list.json");

        assertThat(this.jsonList.write(users)).extractingJsonPathArrayValue("@.[*].id").containsExactly(99, 100, 101, 102, 103, 104);
     
    }

    @Test
    public void userDeserializationTest() throws IOException {
        String expected = """
           {
                "id":99,
                "name":"john",
                "lastName":"doe",
                "email":"john.doe@email.com",
                "birthday":"2000-12-01",
                "phoneNumber":"123456789",
                "address":"1234 Main St",
                "owner":"admin"
           }
           """;
    

        assertThat(this.json.parse(expected))
           .isEqualTo(new User(99L ,"john", "doe", "john.doe@email.com", LocalDate.parse("2000-12-01"), "123456789", "1234 Main St", "admin"));

        assertThat(this.json.parseObject(expected).getId()).isEqualTo(99);
        assertThat(this.json.parseObject(expected).getName()).isEqualTo("john");
        assertThat(this.json.parseObject(expected).getLastName()).isEqualTo("doe");
        assertThat(this.json.parseObject(expected).getEmail()).isEqualTo("john.doe@email.com");
        assertThat(this.json.parseObject(expected).getBirthday()).isEqualTo(LocalDate.parse("2000-12-01"));
        assertThat(this.json.parseObject(expected).getPhoneNumber()).isEqualTo("123456789");
        assertThat(this.json.parseObject(expected).getAddress()).isEqualTo("1234 Main St");
        assertThat(this.json.parseObject(expected).getOwner()).isEqualTo("admin");

 }
    @Test
    public void userListDeserializationTest() throws IOException {
        String expected = """
            [
                {
                    "id":99,
                    "name":"john",
                    "lastName":"doe",
                    "email":"john.doe@email.com",
                    "birthday":"2000-12-01",
                    "phoneNumber":"123456789",
                    "address":"1234 Main St",
                    "owner":"admin"
                },
                {
                    "id":100,
                    "name":"jane",
                    "lastName":"smith",
                    "email":"jane.smith@email.com",
                    "birthday":"1995-05-10",
                    "phoneNumber":"987654321",
                    "address":"456 Elm St",
                    "owner":"admin"
                },
                {
                    "id":101,
                    "name":"alex",
                    "lastName":"brown",
                    "email":"alex.brown@email.com",
                    "birthday":"1988-09-15",
                    "phoneNumber":"555555555",
                    "address":"789 Oak St",
                    "owner":"admin"
                },
                {
                    "id":102,
                    "name":"sarah",
                    "lastName":"johnson",
                    "email":"sarah.johnson@email.com",
                    "birthday":"1992-03-25",
                    "phoneNumber":"111111111",
                    "address":"321 Pine St",
                    "owner":"admin"
                },
                {
                    "id":103,
                    "name":"michael",
                    "lastName":"wilson",
                    "email":"michael.wilson@email.com",
                    "birthday":"1980-11-05",
                    "phoneNumber":"999999999",
                    "address":"654 Cedar St",
                    "owner":"admin"
                },
                {
                    "id":104,
                    "name":"emily",
                    "lastName":"thompson",
                    "email":"emily.thompson@email.com",
                    "birthday":"1998-07-20",
                    "phoneNumber":"444444444",
                    "address":"987 Birch St",
                    "owner":"admin"
                }
            ]
        """;
        assertThat(this.jsonList.parse(expected))
            .isEqualTo(users);
    }
}
