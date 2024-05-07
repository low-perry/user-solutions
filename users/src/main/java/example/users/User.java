package example.users;

import java.time.LocalDate;
import java.util.Objects;

import example.users.validation.Adult;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import org.springframework.data.annotation.Id;
import jakarta.validation.constraints.Email;

public class User {
    @Id 
    private Long id;

    @NotNull(message = "Name cannot be null")
    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotNull(message = "Last name cannot be null")
    @NotBlank(message = "Last name cannot be blank")
    private String lastName;

    @Email(message = "Email should be valid")
    @NotNull(message = "Email cannot be null")
    @NotBlank(message = "Email cannot be blank")
    private String email;

    @NotNull(message = "Birthday cannot be null")
    @Past(message = "Birthday must be in the past")
    @Adult
    private LocalDate birthday;

    private String phoneNumber;

    private String address;

    private String owner;


    public User() {
    }

    public User(Long id, String name, String lastName, String email, LocalDate birthday, String owner) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.birthday = birthday;
        this.owner = owner;
        this.phoneNumber = null;
        this.address = null;
    }
    public User(Long id, String name, String lastName, String email, LocalDate birthday, String phoneNumber, String address, String owner) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.birthday = birthday;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.owner = owner;
    }

    public String getName() {
        return name;
    }
    
    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public Long getId() {
        return id;
    }

    public String getOwner() {
        return owner;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        User user = (User) obj;
        return id == user.id &&
            Objects.equals(name, user.name) &&
            Objects.equals(lastName, user.lastName) &&
            Objects.equals(email, user.email) &&
            Objects.equals(birthday, user.birthday) &&
            Objects.equals(phoneNumber, user.phoneNumber) &&
            Objects.equals(address, user.address) &&
            Objects.equals(owner, user.owner);
    }

}
