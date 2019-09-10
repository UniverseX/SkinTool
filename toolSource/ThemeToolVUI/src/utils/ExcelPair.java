package utils;

import org.apache.poi.ss.usermodel.CellStyle;

import java.util.Objects;

public class ExcelPair{
    public final Object value;
    public final int cellType;
    public final CellStyle cellStyle;

    public ExcelPair(Object value, int cellType, CellStyle cellStyle) {
        this.value = value;
        this.cellType = cellType;
        this.cellStyle = cellStyle;
    }

    /**
     * Checks the two objects for equality by delegating to their respective
     * {@link Object#equals(Object)} methods.
     *
     * @param o the {@link ExcelPair} to which this one is to be checked for equality
     * @return true if the underlying objects of the Pair are both considered
     *         equal
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ExcelPair)) {
            return false;
        }
        ExcelPair p = (ExcelPair) o;
        return Objects.equals(p.value, value) && Objects.equals(p.cellType, cellType);
    }

    @Override
    public String toString() {
        return "value=" + String.valueOf(value);
    }
}
