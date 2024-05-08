package example.users.validation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AgeHelper {

    @Value("${user.age}")
    private int age;

    public int getAge() {
        return age;
    }
}