package example.users.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Period;

import org.springframework.beans.factory.annotation.Autowired;

public class AdultValidator implements ConstraintValidator<Adult, LocalDate> {

    @Autowired
    private AgeHelper ageHelper;

    @Override
    public void initialize(Adult constraintAnnotation) {
    }

    @Override
    public boolean isValid(LocalDate birthday, ConstraintValidatorContext constraintValidatorContext) {
        return birthday != null && Period.between(birthday, LocalDate.now()).getYears() >= ageHelper.getAge();
    }

}
