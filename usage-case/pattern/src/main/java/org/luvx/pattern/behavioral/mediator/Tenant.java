package org.luvx.pattern.behavioral.mediator;

import org.luvx.pattern.behavioral.mediator.base.Mediator;
import org.luvx.pattern.behavioral.mediator.base.Tenantable;

/**
 * 租客
 *
 * @author: Ren, Xie
 */
public class Tenant implements Tenantable {
    protected Mediator mediator;

    public Tenant(Mediator mediator) {
        this.mediator = mediator;
    }

    @Override
    public void lessee() {
        mediator.lessee();
    }
}
