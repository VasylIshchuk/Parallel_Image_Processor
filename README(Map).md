 * XChart Example Code:
      https://knowm.org/open-source/xchart/xchart-example-code/

    
     
*      Map<Integer, Long> counts = imageHistogram.stream()
                            .collect(Collectors.groupingBy(e -> e, Collectors.counting()));
Collectors.groupingBy(e -> e, ...) creates a map where the keys are the values from the stream (so, each unique value from the imageHistogram).
e -> e is a lambda expression, which means that the stream elements themselves become keys in the map.
Collectors.counting() counts how many times each element occurs in the stream and uses that value as the value in the map.
        
      List<Long> frequencyPixels= new ArrayList<>();
 *       for(Map.Entry<Integer,Long> entry : counts.entrySet()){
            frequencyPixels.add(entry.getValue());
        }
'counts.entrySet()' returns the set of all records in the map 'counts'. Each record is a 'Map.Entry' object that contains a key and a value.
'Map.Entry<Integer, Long>' specifies the record type. In this case, the keys of the map are 'Integer' objects, and the values are 'Long' objects.
      
