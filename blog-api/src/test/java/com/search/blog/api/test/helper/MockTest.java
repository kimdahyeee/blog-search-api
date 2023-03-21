package com.search.blog.api.test.helper;

import com.search.blog.api.test.config.TestProfile;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles(TestProfile.TEST)
@ExtendWith(MockitoExtension.class)
public class MockTest {

}
