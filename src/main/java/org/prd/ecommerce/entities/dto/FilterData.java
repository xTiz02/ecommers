package org.prd.ecommerce.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilterData {
    private List<Long> categoriesIds;
    private List<String> colors;
    private Double mayorDe;
    private Double menorDe;
    private Integer index;
    private Integer size;
    private String title;
    @Override
    public String toString() {
        return "FilterData{" +
                "categoriesIds=" + categoriesIds +
                ", colors=" + colors +
                ", mayorDe=" + mayorDe +
                ", menorDe=" + menorDe +
                ", index=" + index +
                ", size=" + size +
                ", title='" + title + '\'' +
                '}';
    }
}
