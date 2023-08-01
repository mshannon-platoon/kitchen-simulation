# Requirements Gathering

### The kitchen 

* It should receive an order every 2 seconds (configurable)
* It should cook the order instantly upon receiving it.
* It should place the order on the best available shelf.
* Upon receiving the order it should dispatch a courier to pick it up randomly between 2-6 seconds later. (ScheduledFuture)

### The orders

* Download the JSON File from the given URL.
* Orders must be parsed from the file and ingested into the system at a rate of 2 orders per second.
* Ingestion rate should be configurable so performance can be tested later.

### Shelves

* Multiple Shelves to hold orders at different temperatures
* Each order should be placed on a shelf to match the order temperature. 
* If the shelf for temperature is full, an order can be placed on the overflow shelf. ( AN ORDER, so select a random one or maybe one with the longest life?)
* If the overflow shelf is full, an existing order of your choosing on the overflow shelf should be moved to an allowable shelf with room,
if no such move is possible, a random order from the overflow shelf should be discarded as waste. ( Will not be available for pickup)

#### SHELF(SPACE)
- HOT(10) decayModifier 1,
- COLD(10) decayModifier 1,
- FROZEN(10) decayModifier 1,
- OVERFLOW(15)  decayModifier 2

### Shelf Life
* Orders deteriorate over time based on shelfLife and decayRate
* Orders that have a value of ZERO are wasted. They should never be delivered and should be removed.
* value = (shelfLife - decayRate * orderAge(in seconds) * shelfDecayModifier) / shelfLife

#### Important: shelfDecayModifier is 1 for single-temperature shelves and 2 for the overflow shelf.

# Improvements and Ideas

1) Shelves - Should we have some thread running, and locking every 5 seconds 
and this should re-organise the shelves, and cancel couriers that are already scheduled?

2) We should definately be tracking how much waste is being produced so we can build some benchmarks

3) Couriers should only be responsible for literally taking food from the kitchen "organiser" they shouldn't be popping items from shelves, the kitchen logistic manager should handle this.