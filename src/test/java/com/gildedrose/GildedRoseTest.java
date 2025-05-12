package com.gildedrose;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;

import org.junit.jupiter.api.Nested;

class GildedRoseTest {
	
	@Test
    void testInitializationValidInputs() {
        Item[] items = new Item[] {new Item("item1", 1, 2), new Item("item2", 1, 2)};
        GildedRose.setItems(items);
        assertTrue(GildedRose.getItems()[0].name.equals("item1"), "first item is item1");
        assertTrue(GildedRose.getItems()[0].sellIn == 1, "First item sellIn is 1");
        assertTrue(GildedRose.getItems()[0].quality == 2, "First item quality is 2");
        assertTrue(GildedRose.getItems()[1].name.equals("item2"), "Second item is item2");
        assertTrue(GildedRose.getItems()[1].sellIn == 1, "Second item sellIn is 1");
        assertTrue(GildedRose.getItems()[1].quality == 2, "Second item quality is 2");
    }
	
	private static Stream<Arguments> provideInvalidInput() {
	    return Stream.of(
	        Arguments.of((Object) null),  
	        Arguments.of((Object) new Item[0]),   
	        Arguments.of((Object) new Item[] {null}),  
	        Arguments.of((Object) new Item[] {new Item(null, 0, 0)}),
	        Arguments.of((Object) new Item[] {new Item("", 0, 0)}),
	        Arguments.of((Object) new Item[] {new Item("Item", 2, -1)}),
	        Arguments.of((Object) new Item[] {new Item("Item", Integer.MIN_VALUE, 1)}),
	        Arguments.of((Object) new Item[] {new Item("Item", 1, Integer.MAX_VALUE)})
	    );
	}
	
	@ParameterizedTest
	@MethodSource("provideInvalidInput")
	void testInitializationInvalidInputs(Item[] input) {
		assertThrows(IllegalArgumentException.class, () -> GildedRose.setItems(input));
	}
	
	@Nested
	class RegularItems {
		
		@Test
		void testUpdateBeforeSellDate() {
			 Item[] items = new Item[] {new Item("regular", 2, 2), new Item("regular", 1, 0)};
			 GildedRose.setItems(items);
			 GildedRose.updateQuality();
			 assertTrue(GildedRose.getItems()[0].quality == 1, "Quality reduced by one");
			 assertTrue(GildedRose.getItems()[0].sellIn == 1, "SellIn reduced by one");
			 assertTrue(GildedRose.getItems()[1].quality == 0, "Quality stays zero");
		 }
		 
		@Test
		void testUpdateOnSellDate() {
			Item[] items = new Item[] {new Item("regular", 0, 2), new Item("regular", 0, 0)};
			GildedRose.setItems(items);
			GildedRose.updateQuality();
			assertTrue(GildedRose.getItems()[0].quality == 0, "Quality reduced by two");
			assertTrue(GildedRose.getItems()[0].sellIn == -1, "SellIn reduced by one");
			assertTrue(GildedRose.getItems()[1].quality == 0, "Quality stays zero");
		}
		
		@Test
		void testUpdateAfterSellDate() {
			Item[] items = new Item[] {new Item("regular", -3, 2), new Item("regular", -2, 0)};
			GildedRose.setItems(items);
			GildedRose.updateQuality();
			assertTrue(GildedRose.getItems()[0].quality == 0, "Quality reduced by two");
			assertTrue(GildedRose.getItems()[0].sellIn == -4, "SellIn reduced by one");
			assertTrue(GildedRose.getItems()[1].quality == 0, "Quality stays zero");
		}
	}
	
	@Nested
	class AgedBrie {
		
		@Test
		void testUpdateBeforeSellDate() {
			Item[] items = new Item[] {new Item("Aged Brie", 2, 2), new Item("Aged Brie", 2, 50)};
			GildedRose.setItems(items);
			GildedRose.updateQuality();
			assertTrue(GildedRose.getItems()[0].quality == 3, "Quality increased by one");
			assertTrue(GildedRose.getItems()[0].sellIn == 1, "SellIn reduced by one");
			assertTrue(GildedRose.getItems()[1].quality == 50, "For quality 50, qality stays the same");
		}
		
		@Test
		void testUpdateOnSellDate() {
			Item[] items = new Item[] {new Item("Aged Brie", 0, 2), new Item("Aged Brie", 0, 50), new Item("Aged Brie", 0, 49)};
			GildedRose.setItems(items);
			GildedRose.updateQuality();
			assertTrue(GildedRose.getItems()[0].quality == 4, "Quality increased by two");
			assertTrue(GildedRose.getItems()[0].sellIn == -1, "SellIn reduced by one");
			assertTrue(GildedRose.getItems()[1].quality == 50, "For quality 50, quality stays the same");
			assertTrue(GildedRose.getItems()[1].quality == 50, "For quality 49, quality increases only by one");
		
		}
		
		@Test
		void testUpdateAfterSellDate() {
			Item[] items = new Item[] {new Item("Aged Brie", -1, 2), new Item("Aged Brie", -1, 50), new Item("Aged Brie", -1, 49)};
			GildedRose.setItems(items);
			GildedRose.updateQuality();
			assertTrue(GildedRose.getItems()[0].quality == 4, "Quality increased by two");
			assertTrue(GildedRose.getItems()[0].sellIn == -2, "SellIn reduced by one");
			assertTrue(GildedRose.getItems()[1].quality == 50, "For quality 50, quality stays the same");
			assertTrue(GildedRose.getItems()[1].quality == 50, "For quality 49, quality increases only by one");
		
		}
	}
		
	@Nested
	class Sulfuras {
	
		@Test
		void testUpdate() {
			Item[] items = new Item[] {new Item("Sulfuras, Hand of Ragnaros", 0, 80),
									new Item("Sulfuras, Hand of Ragnaros", -1, 80),
									new Item("Sulfuras, Hand of Ragnaros", 1, 80)};
			GildedRose.setItems(items);
			GildedRose.updateQuality();
			assertTrue(GildedRose.getItems()[0].quality == 80, "Quality doesn't change on expiry");
			assertTrue(GildedRose.getItems()[0].sellIn == 0, "SellIn doesn't change on expiry");
			assertTrue(GildedRose.getItems()[1].quality == 80, "Quality doesn't change after expiry");
			assertTrue(GildedRose.getItems()[1].sellIn == -1, "SellIn doesn't change after expiry");
			assertTrue(GildedRose.getItems()[2].quality == 80, "Quality doesn't change before expiry");
			assertTrue(GildedRose.getItems()[2].sellIn == 1, "SellIn doesn't before expiry");
		}
	}
	
	@Nested
	class BackstagePasses {
		
		@Test
		void testUpdateBeforeSellDate() {
			Item[] items = new Item[] {new Item("Backstage passes to a TAFKAL80ETC concert", 12, 2),
									new Item("Backstage passes to a TAFKAL80ETC concert", 9, 4),
									new Item("Backstage passes to a TAFKAL80ETC concert", 3, 4),
									new Item("Backstage passes to a TAFKAL80ETC concert", 2, 49),
									new Item("Backstage passes to a TAFKAL80ETC concert", 2, 50)};
			GildedRose.setItems(items);
			GildedRose.updateQuality();
			assertTrue(GildedRose.getItems()[0].quality == 3, "Quality increased by one on more than 10 days remaining");
			assertTrue(GildedRose.getItems()[0].sellIn == 11, "SellIn reduced by one");
			assertTrue(GildedRose.getItems()[1].quality == 6, "Quality increased by two on 10 or less days remaining (more than 5");
			assertTrue(GildedRose.getItems()[1].sellIn == 8, "SellIn reduced by one");
			assertTrue(GildedRose.getItems()[2].quality == 7, "Quality increased by three on 5 days or less remaining");
			assertTrue(GildedRose.getItems()[2].sellIn == 2, "SellIn reduced by one");
			assertTrue(GildedRose.getItems()[3].quality == 50, "Quality increases up to 50 and no more");
			assertTrue(GildedRose.getItems()[4].quality == 50, "Quality stays 50");
			
		}
		
		@Test
		void testUpdateOnSellDate() {
			Item[] items = new Item[] {new Item("Backstage passes to a TAFKAL80ETC concert", 0, 7)};
			GildedRose.setItems(items);
			GildedRose.updateQuality();
			assertTrue(GildedRose.getItems()[0].quality == 0, "Quality drops to zero");
			assertTrue(GildedRose.getItems()[0].sellIn == -1, "SellIn reduced by one");
		}
		
		@Test
		void testUpdateAfterSellDate() {
			Item[] items = new Item[] {new Item("Backstage passes to a TAFKAL80ETC concert", -2, 7)};
			GildedRose.setItems(items);
			GildedRose.updateQuality();
			assertTrue(GildedRose.getItems()[0].quality == 0, "Quality drops to zero");
			assertTrue(GildedRose.getItems()[0].sellIn == -3, "SellIn reduced by one");
		}
	}
	
	@Nested
	class ConjuredItems {
		
		@Test
		void testUpdateBeforeSellDate() {
			 Item[] items = new Item[] {new Item("Conjured 1", 2, 2), new Item("Conjured 2", 1, 0)};
			 GildedRose.setItems(items);
			 GildedRose.updateQuality();
			 assertTrue(GildedRose.getItems()[0].quality == 0, "Quality reduced by two");
			 assertTrue(GildedRose.getItems()[0].sellIn == 1, "SellIn reduced by one");
			 assertTrue(GildedRose.getItems()[1].quality == 0, "Quality stays zero");
		 }
		 
		@Test
		void testUpdateOnSellDate() {
			Item[] items = new Item[] {new Item("Conjured 1", 0, 4), new Item("Conjured 2", 0, 0)};
			GildedRose.setItems(items);
			GildedRose.updateQuality();
			assertTrue(GildedRose.getItems()[0].quality == 0, "Quality reduced by four");
			assertTrue(GildedRose.getItems()[0].sellIn == -1, "SellIn reduced by one");
			assertTrue(GildedRose.getItems()[1].quality == 0, "Quality stays zero");
		}
		
		@Test
		void testUpdateAfterSellDate() {
			Item[] items = new Item[] {new Item("Conjured 1", -3, 4), new Item("Conjured 2", -2, 0)};
			GildedRose.setItems(items);
			GildedRose.updateQuality();
			assertTrue(GildedRose.getItems()[0].quality == 0, "Quality reduced by four");
			assertTrue(GildedRose.getItems()[0].sellIn == -4, "SellIn reduced by one");
			assertTrue(GildedRose.getItems()[1].quality == 0, "Quality stays zero");
		}
	}
}
