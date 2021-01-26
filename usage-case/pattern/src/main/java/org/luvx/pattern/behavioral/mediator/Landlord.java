package org.luvx.pattern.behavioral.mediator;

import org.luvx.pattern.behavioral.mediator.base.Mediator;
import org.luvx.pattern.behavioral.mediator.base.Rentable;

/**
 * 房东
 *
 * @author: Ren, Xie
 */
public class Landlord implements Rentable {
    protected Mediator mediator;

    public Landlord(Mediator mediator) {
        this.mediator = mediator;
    }

    @Override
    public void rental() {
        mediator.rental();
    }
}
