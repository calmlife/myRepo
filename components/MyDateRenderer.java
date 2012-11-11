package components;

import java.awt.Component;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class MyDateRenderer extends DefaultTableCellRenderer{
	private SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy h:mm a Z");
	public MyDateRenderer(SimpleDateFormat df){
		this.df=df;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table,
            Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        if( value instanceof Date) {
            value = df.format(value);
        }
        return super.getTableCellRendererComponent(table, value, isSelected,
                hasFocus, row, column);
    }

}
