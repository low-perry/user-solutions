package example.users;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jdbc.repository.query.Query;

@Repository
interface UserRepository extends CrudRepository<User, Long>, PagingAndSortingRepository<User, Long> {

    User findByIdAndOwner(Long id, String owner);

    Page<User> findByOwner(String owner, PageRequest pageRequest);

    boolean existsByIdAndOwner(Long id, String owner);


    @Query("SELECT * FROM \"USER\" WHERE birthday BETWEEN :startDate AND :endDate AND owner = :owner")
    List<User> findByOwnerAndBetweenDates(@Param("owner") String owner, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);



    
}
