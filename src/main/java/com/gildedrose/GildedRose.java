package com.gildedrose;

final class GildedRose {
	
    private static Item[] items;
    
	private GildedRose() {
    }
    
    public static Item[] getItems() {
		return items;
	}

	public static void setItems(Item[] items) {
		if (validateInputs(items) == true) {
			GildedRose.items = items;
		}
	}

	private static boolean validateInputs(Item[] items) {
		if (items == null) {
			throw new IllegalArgumentException("Item list can't be null!");
		}
		
		if (items.length == 0) {
			throw new IllegalArgumentException("Item list can't be empty!");
		}
		
		for (Item i : items) {
			if (i == null) {
				throw new IllegalArgumentException("Item can't be null");
			}
			if (i.name == null) {
				throw new IllegalArgumentException("Item name can't be null");
			}
			if (i.name == "") {
				throw new IllegalArgumentException("Item name can't be empty");
			}
			if (i.quality < 0) {
				throw new IllegalArgumentException("Item can't have negative quality");
			}
			if (i.quality == Integer.MAX_VALUE || i.sellIn == Integer.MIN_VALUE) {
				throw new IllegalArgumentException("Integer overflow!");
			}
		}
		
		return true;
	}

    public static void updateQuality() {

        for (Item item : items) {
        	updateItemQuality(item);
        	
            if (item.sellIn < 0) {
                handleExpiredItem(item);
            }
        }
    }

	private static void updateItemQuality(Item item) {
		
		switch(item.name) {
		
		//If item is Sulfuras it doesn't change
		case "Sulfuras, Hand of Ragnaros":
			break;
		
		//If item is Aged Brie it gains 1 quality (up to 50) and advances sellIn by 1 
		case "Aged Brie":
			if (item.quality < 50) {
				item.quality++;
			}
			item.sellIn--;
			break;
		
		//If item is Backstage passes it gains 1, 2 or 3 in quality based on when it expires (up to 50) and advances sellIn by 1
		case "Backstage passes to a TAFKAL80ETC concert":
			if (item.quality < 50) {
				item.quality++;
				if (item.quality < 50 && item.sellIn < 11) {
					item.quality++;
				}
				if (item.quality < 50 && item.sellIn < 6) {
					item.quality++;
				}
			}
			item.sellIn--;
			break;
		//Every other item loses 1 quality and advances it's sellIn by 1 (Conjured items lose double)
		default:
			if (item.quality > 0) {
				item.quality--;
			}
			if (item.name.startsWith("Conjured") && item.quality > 0) {
				item.quality--;
			}
			item.sellIn--;
			break;
		}
	}

	private static void handleExpiredItem(Item item) {
		
		switch(item.name) {
		
		//If item is Aged Brie and quality is < 50 then it's quality increases by 1 more
		case "Aged Brie":
			if (item.quality < 50) {
				item.quality++;
			}
			break;
			
		//If item is Backstage passes then it loses all value
		case "Backstage passes to a TAFKAL80ETC concert":
			item.quality = 0;
			break;
			
		//If item is Sulfuras, it does not change
		case "Sulfuras, Hand of Ragnaros":
			break;
			
		//Everything else loses 1 more in quality
		default:
			if (item.quality > 0) {
				item.quality--;  
			}
			if (item.name.startsWith("Conjured") && item.quality > 0) {
				item.quality--;
			}
			break;
		}
	}
}
