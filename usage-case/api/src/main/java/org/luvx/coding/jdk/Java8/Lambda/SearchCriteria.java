package org.luvx.coding.jdk.Java8.Lambda;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

/**
 * ��ѯ��׼
 * @author MikeW
 */
public class SearchCriteria {
	private final Map<String, Predicate<Person>> searchMap = new HashMap<>();

	private SearchCriteria() {
		super();
		initSearchMap();
	}

	public static SearchCriteria getInstance() {
		return new SearchCriteria();
	}

	private void initSearchMap() {
		Predicate<Person> allDrivers = p -> p.getAge() >= 16;
		Predicate<Person> allDraftees = p -> p.getAge() >= 18 && p.getAge() <= 25 && p.getGender() == Gender.MALE;
		Predicate<Person> allPilots = p -> p.getAge() >= 23 && p.getAge() <= 65;

		searchMap.put("allDrivers", allDrivers);
		searchMap.put("allDraftees", allDraftees);
		searchMap.put("allPilots", allPilots);

	}

	public Predicate<Person> getCriteria(String PredicateName) {
		Predicate<Person> target;
		target = searchMap.get(PredicateName);

		if (target == null) {
			System.out.println("Search Criteria not found... ");
			System.exit(1);
		}

		return target;
	}
}
