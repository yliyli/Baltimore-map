# HashTable Discussion

Explain which of your implementations (between OpenAddressingHashMap and ChainingHashMap) 
is to use for the search engine.

- Which approaches did you try in the implementation of each hash table 
(e.g. probing strategies, rehashing strategies, etc), in what order, and why did you choose them? 
What specific tweaks to your implementation improved or made things worse? Include failed or 
abandoned attempts if any, and why. Summarize all the different ways you developed, evaluated 
and improved your hash tables.

For chaining hashtable, I did it in standard way without much changing. I created the array for hashmap, with each element having a pointer, so I could make a linked list
myself with this pointer. For openAddressing hashtable, I first simply tested the linear probing, which worked out horribly (I thought it would work out fine). Then I tried quadratic
probing, which, although took a bit more space, reduced the runtime significantly. I really hope I could do more testing, but I was traveling back to China and have little time to implement more for
this homework...

- Include all the benchmarking data, results and analysis that contributed to your final 
decision on which implementation to use for the search engine.

JDK:
Index Created!
Processed jhu.txt in 32 ms using 1290 kb memory.

Index Created!
Processed apache.txt in 593 ms using 116246 kb memory.

Chain:
Index Created!
Processed jhu.txt in 27 ms using 1290 kb memory.

Index Created!
Processed apache.txt in 6764 ms using 173755 kb memory.

Open Address:
Index Created!
Processed jhu.txt in 32 ms using 1290 kb memory.

Index Created!
Processed apache.txt in 58665 ms using 168616 kb memory.


Then I changed the load factor from 0.75 to 0.5:
Chain:
Index Created!
Processed apache.txt in 9512 ms using 170785 kb memory.

Open Address:
Index Created!
Processed apache.txt in 40756 ms using 165678 kb memory.

Then I changed to quadratic probing for open addressing:
Index Created!
Processed apache.txt in 15594 ms using 165565 kb memory.


- Provide an analysis of your benchmark data and conclusions. Why did you choose your final HashMap 
implementation as the best one? What results were surprising and which were expected?

So, for jhu.test every method spend similar time, probably because of it being very short
However, when I moved to larger file, apache.txt, my two methods spend much more time than the 
JDK hashmap. The chaining was long, but openaddressing was much longer. I think this is due to the fact that 
chaining have the advantage of not having to worry about tombstones, while my openaddressing have to worry both about contamination,
and primary clustering. 

So I changed the load factor from 0.75 to 0.5. The change made chaining slower, actually, because like I said, chaining makes the hashmap 
doesn't have to worry about contamination and clustering, so a higher load factor would be more efficient in time and space, so I put my chaining 
hashmap load factor back to 0.75. As for open addressing, however, it significantly reduced the runtime from 60k ms to 40kms, which is pretty amazing,
showing how 0.75 is a bit too much for load factor, and is creating a lot of contamination and clustering (along with linear probing). Then I finally changed
linear probing to quadratic probing, and this move was very effective, cutting the runtime for openaddressing down to 15k ms, which really showed how much primary clustering
was haunting me in previous attemps.

Sorry I don't have more time for more runs. In general, for now chaining works better, but both method have ways to improve. 