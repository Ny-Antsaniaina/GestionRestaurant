package org.example.DAO.CrudOperation;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Filter {
    private String column;
    private Object valueIntervalMin;
    private Object valueIntervalMax;
    private String operator;

    public Filter(String column, Object value,  String operator) {
        this.column = column;
        this.valueIntervalMin = value;
        this.operator = operator;
    }
    public Filter(String column, Object valueIntervalMin, Object valueIntervalMax) {
        this.column = column;
        this.valueIntervalMin = valueIntervalMin;
        this.valueIntervalMax = valueIntervalMax;
        this.operator = "BETWEEN";
    }
}
