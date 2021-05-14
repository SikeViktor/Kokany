package bau.kokany.model;

import org.hibernate.mapping.Collection;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class JPAExpertDAOTest {

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Mock
    JPAExpertDAO expertMock;


    @Test
    public void getExperts() {
        List<Expert> lista = new ArrayList<>();

        assertEquals(lista, expertMock.getExperts());
    }
}