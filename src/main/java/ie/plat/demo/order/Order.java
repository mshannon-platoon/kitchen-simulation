package ie.plat.demo.order;


import java.util.Date;

public record Order(String id,
                    String name,
                    String temp,
                    Integer shelfLife,
                    float decayRate,
                    Date createdDate) { }
