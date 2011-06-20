import java.io.Serializable;
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.event.ListDataEvent;

public class AlwaysSelectableComboBox extends JComboBox implements
Serializable
{
	private static final long serialVersionUID = -980261447559944836L;
	private boolean _alwaysFireOnSelect = true;

	public AlwaysSelectableComboBox(ComboBoxModel aModel)  {super(aModel); }
	public AlwaysSelectableComboBox(final Object items[])  {super(items); }
	public AlwaysSelectableComboBox(Vector items)          {super(items); }
	public AlwaysSelectableComboBox()                      {super(); }

	public void setAlwaysFireOnSelect(boolean alwaysFireOnSelect) {
		_alwaysFireOnSelect = alwaysFireOnSelect;
	}

	public void contentsChanged(ListDataEvent e) {
		if (_alwaysFireOnSelect) {
			selectedItemReminder = null;
		}

		super.contentsChanged(e);
	}
}

// http://www.devx.com/tips/Tip/5329