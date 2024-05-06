package example.users;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

interface UserRepository extends CrudRepository<User, Long>, PagingAndSortingRepository<User, Long> {

    User findByIdAndOwner(Long id, String owner);
    Page<User> findByOwner(String owner, PageRequest pageRequest);
    boolean existsByIdAndOwner(Long id, String owner);

    
}
