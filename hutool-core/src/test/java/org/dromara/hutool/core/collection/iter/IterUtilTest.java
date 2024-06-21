/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.collection.iter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.collection.ListUtil;
import org.dromara.hutool.core.collection.set.SetUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.w3c.dom.NodeList;

import java.util.*;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

/**
 * test for {@link IterUtil}
 */
public class IterUtilTest {

	@Test
	public void testGetIter() {
		Assertions.assertNull(IterUtil.getIter(null));
		assertEquals(Collections.emptyIterator(), IterUtil.getIter(Collections.emptyList()));

		Assertions.assertNull(IterUtil.getIter((Object)null));
		Assertions.assertNotNull(IterUtil.getIter(Collections.emptyIterator()));
		Assertions.assertNotNull(IterUtil.getIter((Object)Collections.emptyList()));
		Assertions.assertNotNull(IterUtil.getIter(new Integer[0]));
		Assertions.assertNotNull(IterUtil.getIter(Collections.emptyMap()));
		Assertions.assertNull(IterUtil.getIter((NodeList)null));
	}

	@Test
	public void testIsEmpty() {
		assertTrue(IterUtil.isEmpty(Collections.emptyIterator()));
		assertFalse(IterUtil.isEmpty(Arrays.asList(1, 2).iterator()));

		assertTrue(IterUtil.isEmpty(Collections.emptyList()));
		assertFalse(IterUtil.isEmpty(Arrays.asList(1, 2)));
	}

	@Test
	public void testIsNotEmpty() {
		assertFalse(IterUtil.isNotEmpty(Collections.emptyIterator()));
		assertTrue(IterUtil.isNotEmpty(Arrays.asList(1, 2).iterator()));

		assertFalse(IterUtil.isNotEmpty(Collections.emptyList()));
		assertTrue(IterUtil.isNotEmpty(Arrays.asList(1, 2)));
	}

	@Test
	public void testHasNull() {
		assertFalse(IterUtil.hasNull(Arrays.asList(1, 3, 2).iterator()));
		assertTrue(IterUtil.hasNull(Arrays.asList(1, null, 2).iterator()));
		assertFalse(IterUtil.hasNull(Collections.emptyIterator()));
		assertTrue(IterUtil.hasNull(null));
	}

	@Test
	public void testIsAllNull() {
		assertTrue(IterUtil.isAllNull(Arrays.asList(null, null)));
		assertFalse(IterUtil.isAllNull(Arrays.asList(1, null)));
		assertTrue(IterUtil.isAllNull((Iterable<?>)null));
		assertTrue(IterUtil.isAllNull(Arrays.asList(null, null).iterator()));
		assertFalse(IterUtil.isAllNull(Arrays.asList(1, null).iterator()));
		assertTrue(IterUtil.isAllNull((Iterator<?>)null));
	}

	@Test
	public void testCountMap() {
		final Object o1 = new Object();
		final Object o2 = new Object();
		final Map<Object, Integer> countMap = IterUtil.countMap(Arrays.asList(o1, o2, o1, o1).iterator());
		assertEquals((Integer)3, countMap.get(o1));
		assertEquals((Integer)1, countMap.get(o2));
	}

	@Test
	public void testFieldValueMap() {
		final Bean bean1 = new Bean(1, "A");
		final Bean bean2 = new Bean(2, "B");
		final Map<Integer, Bean> map = IterUtil.fieldValueMap(Arrays.asList(bean1, bean2).iterator(), "id");
		assertEquals(bean1, map.get(1));
		assertEquals(bean2, map.get(2));
	}

	@Test
	public void testFieldValueAsMap() {
		final Bean bean1 = new Bean(1, "A");
		final Bean bean2 = new Bean(2, "B");
		final Map<Integer, String> map = IterUtil.fieldValueAsMap(
			Arrays.asList(bean1, bean2).iterator(), "id", "name"
		);
		assertEquals("A", map.get(1));
		assertEquals("B", map.get(2));
	}

	@Test
	public void testFieldValueList() {
		final Bean bean1 = new Bean(1, "A");
		final Bean bean2 = new Bean(2, "B");
		assertEquals(Arrays.asList(1, 2), IterUtil.fieldValueList(Arrays.asList(bean1, bean2), "id"));
		assertEquals(
			Arrays.asList(1, 2),
			IterUtil.fieldValueList(Arrays.asList(bean1, bean2).iterator(), "id")
		);
	}

	@Test
	public void testJoin() {
		final List<String> stringList = Arrays.asList("1", "2", "3");
		assertEquals("123", IterUtil.join(stringList.iterator(), ""));
		assertEquals("-1--2--3-", IterUtil.join(stringList.iterator(), "", "-", "-"));
		assertEquals("123", IterUtil.join(stringList.iterator(), "", Function.identity()));
	}

	@Test
	public void testToMap() {
		final List<Integer> keys = Arrays.asList(1, 2, 3);

		Map<Integer, Integer> map = IterUtil.toMap(keys, keys);
		assertEquals(keys, new ArrayList<>(map.keySet()));
		assertEquals(keys, new ArrayList<>(map.values()));

		map = IterUtil.toMap(keys.iterator(), keys.iterator());
		assertEquals(keys, new ArrayList<>(map.keySet()));
		assertEquals(keys, new ArrayList<>(map.values()));

		map = IterUtil.toMap(keys.iterator(), keys.iterator(), true);
		assertEquals(keys, new ArrayList<>(map.keySet()));
		assertEquals(keys, new ArrayList<>(map.values()));

		map = IterUtil.toMap(keys, keys, true);
		assertEquals(keys, new ArrayList<>(map.keySet()));
		assertEquals(keys, new ArrayList<>(map.values()));

		map = IterUtil.toMap(keys, Function.identity());
		assertEquals(keys, new ArrayList<>(map.keySet()));
		assertEquals(keys, new ArrayList<>(map.values()));

		map = IterUtil.toMap(keys, Function.identity(), Function.identity());
		assertEquals(keys, new ArrayList<>(map.keySet()));
		assertEquals(keys, new ArrayList<>(map.values()));
	}

	@Test
	public void testToListMap() {
		final List<Integer> keys = Arrays.asList(1, 2, 3, 4);

		Map<Boolean, List<Integer>> map = IterUtil.toListMap(keys, i -> (i & 1) == 0, Function.identity());
		assertEquals(Arrays.asList(2, 4), map.get(true));
		assertEquals(Arrays.asList(1, 3), map.get(false));

		map = IterUtil.toListMap(keys, i -> (i & 1) == 0);
		assertEquals(Arrays.asList(2, 4), map.get(true));
		assertEquals(Arrays.asList(1, 3), map.get(false));

		map = new LinkedHashMap<>();
		final Map<Boolean, List<Integer>> rawMap = IterUtil.toListMap(map, keys, i -> (i & 1) == 0, Function.identity());
		Assertions.assertSame(rawMap, map);
		assertEquals(Arrays.asList(2, 4), rawMap.get(true));
		assertEquals(Arrays.asList(1, 3), rawMap.get(false));
	}

	@Test
	public void testToListMap2() {
		final Map<String, List<String>> expectedMap = new HashMap<>();
		expectedMap.put("a", Collections.singletonList("and"));
		expectedMap.put("b", Arrays.asList("brave", "back"));

		final Map<String, List<String>> testMap = IterUtil.toListMap(Arrays.asList("and", "brave", "back"),
			v -> v.substring(0, 1));
		assertEquals(testMap, expectedMap);
	}

	@SuppressWarnings("DataFlowIssue")
	@Test
	public void testAsIterable() {
		final Iterator<Integer> iter = Arrays.asList(1, 2, 3).iterator();
		assertEquals(iter, IterUtil.asIterable(iter).iterator());
		Assertions.assertNull(IterUtil.asIterable(null).iterator());

		final Enumeration<Integer> enumeration = new IteratorEnumeration<>(iter);
		final Iterator<Integer> iter2 = IterUtil.asIterator(enumeration);
		assertEquals((Integer)1, iter2.next());
		assertEquals((Integer)2, iter2.next());
		assertEquals((Integer)3, iter2.next());
	}

	@Test
	public void testGet() {
		final Iterator<Integer> iter = Arrays.asList(1, 2, 3, 4).iterator();
		assertEquals((Integer)3, IterUtil.get(iter, 2));
		Assertions.assertThrows(IllegalArgumentException.class, () -> IterUtil.get(iter, -1));
	}

	@Test
	public void testGetFirst() {
		final Iterator<Integer> iter = Arrays.asList(1, 2, 3, 4).iterator();
		assertEquals((Integer)1, IterUtil.getFirst(iter));
		Assertions.assertNull(IterUtil.getFirst(null));
		Assertions.assertNull(IterUtil.getFirst(Collections.emptyIterator()));

		assertEquals((Integer)2, IterUtil.getFirst(iter, t -> (t & 1) == 0));
		Assertions.assertNull(IterUtil.getFirst((Iterator<Integer>)null, t -> (t & 1) == 0));
		Assertions.assertNull(IterUtil.getFirst(Collections.emptyIterator(), Objects::nonNull));
	}

	@Test
	public void testGetFirstNoneNull() {
		final Iterator<Integer> iter = Arrays.asList(null, 2, null, 4).iterator();
		assertEquals((Integer)2, IterUtil.getFirstNoneNull(iter));
		Assertions.assertNull(IterUtil.getFirstNoneNull(null));
		Assertions.assertNull(IterUtil.getFirstNoneNull(Collections.emptyIterator()));
	}

	@Test
	public void testGetElementType() {
		final List<Object> list = Arrays.asList(null, "str", null);
		assertEquals(String.class, IterUtil.getElementType(list));
		Assertions.assertNull(IterUtil.getElementType((Iterable<?>)null));
		Assertions.assertNull(IterUtil.getElementType(Collections.emptyList()));

		assertEquals(String.class, IterUtil.getElementType(list.iterator()));
		Assertions.assertNull(IterUtil.getElementType((Iterator<?>)null));
		Assertions.assertNull(IterUtil.getElementType(Collections.emptyIterator()));
	}

	@Test
	public void testEdit() {
		assertEquals(
			Collections.singletonList("str"),
			IterUtil.edit(Arrays.asList(null, "str", null).iterator(), t -> t)
		);
		assertEquals(
			Collections.singletonList("str"),
			IterUtil.edit(Arrays.asList(null, "str", null).iterator(), null)
		);
		assertEquals(Collections.emptyList(), IterUtil.edit(null, t -> t));
	}

	@Test
	public void testRemove() {
		final List<Integer> list = new ArrayList<>(Arrays.asList(1, null, null, 3));
		IterUtil.remove(list.iterator(), Objects::isNull);
		assertEquals(Arrays.asList(1, 3), list);
	}

	@Test
	public void testFilterToList() {
		final List<Integer> list1 = new ArrayList<>(Arrays.asList(1, null, null, 3));
		final List<Integer> list2 = IterUtil.filterToList(list1.iterator(), Objects::nonNull);
		assertEquals(Arrays.asList(1, 3), list2);
	}

	@Test
	public void testFiltered() {
		Assertions.assertNotNull(IterUtil.filtered(Collections.emptyIterator(), t -> true));
	}

	@Test
	public void testEmpty() {
		Assertions.assertSame(Collections.emptyIterator(), IterUtil.empty());
	}

	@Test
	public void testTrans() {
		Assertions.assertNotNull(IterUtil.trans(Collections.emptyIterator(), t -> true));
	}

	@Test
	public void testSize() {
		assertEquals(0, IterUtil.size((Iterator<?>)null));
		assertEquals(0, IterUtil.size(Collections.emptyIterator()));
		assertEquals(3, IterUtil.size(Arrays.asList(1, 2, 3).iterator()));

		assertEquals(0, IterUtil.size((Iterable<?>)null));
		assertEquals(0, IterUtil.size(Collections.emptyList()));
		assertEquals(3, IterUtil.size(Arrays.asList(1, 2, 3)));
	}

	@Test
	public void testIsEqualList() {
		assertFalse(IterUtil.isEqualList(null, Collections.emptyList()));
		assertFalse(IterUtil.isEqualList(Arrays.asList(1, 2, 3), Collections.emptyList()));
		assertFalse(IterUtil.isEqualList(Arrays.asList(1, 2, 3), Arrays.asList(1, 2)));
		assertTrue(IterUtil.isEqualList(Arrays.asList(1, 2, 3), Arrays.asList(1, 2, 3)));
		assertTrue(IterUtil.isEqualList(null, null));
		assertTrue(IterUtil.isEqualList(Collections.emptyList(), Collections.emptyList()));
	}

	@Test
	public void testClear() {
		final List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3));
		IterUtil.clear(list.iterator());
		assertTrue(list.isEmpty());
		Assertions.assertThrows(UnsupportedOperationException.class, () -> IterUtil.clear(Arrays.asList(1, 2).iterator()));
	}

	@Test
	public void testToStr() {
		final List<Integer> list = Arrays.asList(1, 2, 3);
		assertEquals("[1, 2, 3]", IterUtil.toStr(list.iterator()));
		assertEquals("[1, 2, 3]", IterUtil.toStr(list.iterator(), Objects::toString));
		assertEquals("{1:2:3}", IterUtil.toStr(list.iterator(), Objects::toString, ":", "{", "}"));
	}

	@Test
	public void testForEach() {
		final List<Integer> list = new ArrayList<>();
		IterUtil.forEach(Arrays.asList(1, 2, 3, 4).iterator(), list::add);
		assertEquals(Arrays.asList(1, 2, 3, 4), list);
	}

	@SuppressWarnings("unused")
	@RequiredArgsConstructor
	private static class Bean {
		private final Integer id;
		private final String name;
	}

	@Test
	public void getFirstNonNullTest() {
		final List<String> strings = ListUtil.of(null, null, "123", "456", null);
		assertEquals("123", CollUtil.getFirstNoneNull(strings));
	}

	@Test
	public void fieldValueMapTest() {
		final List<Car> carList = ListUtil.of(
			new Car("123", "大众"),
			new Car("345", "奔驰"),
			new Car("567", "路虎"));
		final Map<String, Car> carNameMap = IterUtil.fieldValueMap(carList.iterator(), "carNumber");

		assertEquals("大众", carNameMap.get("123").getCarName());
		assertEquals("奔驰", carNameMap.get("345").getCarName());
		assertEquals("路虎", carNameMap.get("567").getCarName());
	}

	@Test
	public void joinTest() {
		final List<String> list = ListUtil.of("1", "2", "3", "4");
		final String join = IterUtil.join(list.iterator(), ":");
		assertEquals("1:2:3:4", join);

		final List<Integer> list1 = ListUtil.of(1, 2, 3, 4);
		final String join1 = IterUtil.join(list1.iterator(), ":");
		assertEquals("1:2:3:4", join1);

		// 包装每个节点
		final List<String> list2 = ListUtil.of("1", "2", "3", "4");
		final String join2 = IterUtil.join(list2.iterator(), ":", "\"", "\"");
		assertEquals("\"1\":\"2\":\"3\":\"4\"", join2);
	}

	@Test
	public void joinWithFuncTest() {
		final List<String> list = ListUtil.of("1", "2", "3", "4");
		final String join = IterUtil.join(list.iterator(), ":", String::valueOf);
		assertEquals("1:2:3:4", join);
	}

	@Test
	public void joinWithNullTest() {
		final List<String> list = ListUtil.of("1", null, "3", "4");
		final String join = IterUtil.join(list.iterator(), ":", String::valueOf);
		assertEquals("1:null:3:4", join);
	}

	@Test
	public void testToMap2() {
		final Map<String, Car> expectedMap = new HashMap<>();

		final Car bmw = new Car("123", "bmw");
		expectedMap.put("123", bmw);

		final Car benz = new Car("456", "benz");
		expectedMap.put("456", benz);

		final Map<String, Car> testMap = IterUtil.toMap(Arrays.asList(bmw, benz), Car::getCarNumber);
		assertEquals(expectedMap, testMap);
	}

	@Test
	public void getElementTypeTest() {
		final List<Integer> integers = Arrays.asList(null, 1);
		final Class<?> elementType = IterUtil.getElementType(integers);
		assertEquals(Integer.class, elementType);
	}

	@Data
	@AllArgsConstructor
	public static class Car {
		private String carNumber;
		private String carName;
	}

	@Test
	public void removeTest() {
		final List<String> obj2 = ListUtil.of("3");
		final List<String> obj = ListUtil.of("1", "3");

		IterUtil.remove(obj.iterator(), (e)-> !obj2.contains(e));

		assertEquals(1, obj.size());
		assertEquals("3", obj.get(0));
	}

	@Test
	public void filteredTest() {
		final List<String> obj2 = ListUtil.of("3");
		final List<String> obj = ListUtil.of("1", "3");

		final FilterIter<String> filtered = IterUtil.filtered(obj.iterator(), obj2::contains);

		assertEquals("3", filtered.next());
		assertFalse(filtered.hasNext());
	}

	@Test
	public void filterToListTest() {
		final List<String> obj2 = ListUtil.of("3");
		final List<String> obj = ListUtil.of("1", "3");

		final List<String> filtered = IterUtil.filterToList(obj.iterator(), obj2::contains);

		assertEquals(1, filtered.size());
		assertEquals("3", filtered.get(0));
	}

	@Test
	public void getTest() {
		final HashSet<String> set = SetUtil.ofLinked("A", "B", "C", "D");
		final String str = IterUtil.get(set.iterator(), 2);
		assertEquals("C", str);
	}

	@Test
	public void testIsSubTrue() {
		final List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
		assertTrue(IterUtil.isSub(list, list), "Expected a list to be a subset of itself");

		final List<Integer> subList = Arrays.asList(1, 2, 3);
		assertTrue(IterUtil.isSub(subList, list), "Expected subList to be a subset of list");

		assertFalse(IterUtil.isSub(list, subList), "Expected subList to not be a subset of list due to extra elements");
	}

	@Test
	public void testIsSubFalseDueToMissingElements() {
		final List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
		final List<Integer> subList = Arrays.asList(1, 2, 6);
		assertFalse(IterUtil.isSub(subList, list), "Expected subList to not be a subset of list due to missing elements");
	}

	@Test
	public void testIsSubWithEmptySubList() {
		final List<Integer> list = Arrays.asList(1, 2, 3);
		final List<Integer> subList = Collections.emptyList();
		assertTrue(IterUtil.isSub(subList, list), "Expected an empty subList to be considered a subset of list");

		assertFalse(IterUtil.isSub(list, subList), "Expected subList to not be a subset of an empty list");
	}

	@Test
	public void testIsSubWithNullIterable() {
		final List<Integer> list = Arrays.asList(1, 2, 3);
		assertFalse(IterUtil.isSub(null, list), "Expected null to not be considered a subset of list");
		assertFalse(IterUtil.isSub(list, null), "Expected list to not be considered a subset of null");
		assertTrue(IterUtil.isSub(null, null), "Expected null to not be considered a subset of null");
	}
}
