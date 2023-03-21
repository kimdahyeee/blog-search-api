package test.helper;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import test.config.TestProfile;

@ActiveProfiles(TestProfile.TEST)
@DataJpaTest
public class RepositoryTest {

}
