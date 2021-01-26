package org.luvx.pattern.behavioral.mediator;

import org.junit.Test;
import org.luvx.pattern.behavioral.mediator.base.Mediator;

public class MediatorTest {

    @Test
    public void test1() {
        Mediator mediator = new Mediator_A();

        Landlord landlord = new Landlord(mediator);
        Tenant tenant = new Tenant(mediator);

        landlord.rental();
        tenant.lessee();
    }
}