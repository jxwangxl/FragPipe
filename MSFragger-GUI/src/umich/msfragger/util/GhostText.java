/*
 * Copyright (C) 2018 Dmitry Avtonomov
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package umich.msfragger.util;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * From: http://stackoverflow.com/questions/10506789/how-to-display-faint-gray-ghost-text-in-a-jtextfield
 */
public class GhostText implements FocusListener, DocumentListener, PropertyChangeListener {

  private final JTextField textfield;
  private boolean isEmpty;
  private Color ghostTextColor;
  private Color normalTextColor;
  private final String ghostText;

  public static final Color LIGHT_GREY = Color.LIGHT_GRAY;

  public GhostText(final JTextField textfield, String ghostText, Color ghostTextColor) {
    this.textfield = textfield;
    this.ghostText = ghostText;
    this.ghostTextColor = ghostTextColor;
    this.normalTextColor = textfield.getForeground();
    textfield.addFocusListener(this);
    registerListeners();
    updateColor();
    if (!this.textfield.hasFocus()) {
      focusLost(null);
    }
  }

  public static void register(JTextField textfield, String ghostText, Color ghostTextColor) {
    new GhostText(textfield, ghostText, ghostTextColor);
  }

  public void delete() {
    unregisterListeners();
    textfield.removeFocusListener(this);
  }

  private void registerListeners() {
    textfield.getDocument().addDocumentListener(this);
    textfield.addPropertyChangeListener("foreground", this);
  }

  private void unregisterListeners() {
    textfield.getDocument().removeDocumentListener(this);
    textfield.removePropertyChangeListener("foreground", this);
  }

  @Override
  public void focusGained(FocusEvent e) {
    if (isEmpty()) {
      unregisterListeners();
      updateText("", normalTextColor);
      registerListeners();
    }

  }

  @Override
  public void focusLost(FocusEvent e) {
    if (isEmpty()) {
      unregisterListeners();
      updateText(ghostText, ghostTextColor);
      registerListeners();
    }
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    updateColor();
  }

  @Override
  public void changedUpdate(DocumentEvent e) {
    updateColor();
  }

  @Override
  public void insertUpdate(DocumentEvent e) {
    changedUpdate(e);
  }

  @Override
  public void removeUpdate(DocumentEvent e) {
    changedUpdate(e);
  }

  private boolean isEmpty() {
    return StringUtils.isNullOrWhitespace(textfield.getText()) || ghostText
        .equals(textfield.getText());
  }

  private void updateText(String text, Color color) {
    textfield.setText(text);
    textfield.setForeground(color);
  }

  private void updateColor() {
    textfield.setForeground(normalTextColor);
  }
}
