package org.luvx.coding.jdk.Java8.Lambda;

import java.util.List;

/**
 *
 * @author MikeW
 */
public class Test01ForEach {
	public static void main(String[] args) {
		List<Person> pl = Person.createShortList();

		System.out.println("=== Western Phone List ===");
		pl.forEach(p -> p.printWesternName());

		System.out.println("\n=== Eastern Phone List ===");
		pl.forEach(Person::printEasternName);

		System.out.println("\n=== Custom Phone List ===");
		pl.forEach(p -> {
			System.out.println(p.printCustom(r -> "Name: " + r.getGivenName() + " EMail: " + r.getEMail()));
		});

	}

}
