/* Copyright 2014 by Martin Gijsen (www.DeAnalist.nl)
 *
 * This file is part of the PowerTools engine.
 *
 * The PowerTools engine is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * The PowerTools engine is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with the PowerTools engine. If not, see <http://www.gnu.org/licenses/>.
 */

package org.powertools.engine.core;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


public final class ModelRunner extends javax.swing.JFrame implements ActionListener, DocumentListener {
    private JLabel mModelFileLabel;
    private JTextField mModelFileField;
    private JButton mBrowseButton;
    private JFileChooser mFileChooser;
    private JLabel mStopConditionLabel;
    private JRadioButton mEndNodeRadioButton;
    private JRadioButton mAllNodesRadioButton;
    private JRadioButton mAllEdgesRadioButton;
    private JRadioButton mNeverRadioButton;
    private JLabel mSelectorLabel;
    private JRadioButton mRandomRadioButton;
    private JRadioButton mWeightedRadioButton;
    private JButton mGoButton;

    private String mStopCondition;
    private String mSelector;


    public static void main (String args[]) {
        run ();
    }

    static void run () {
        java.awt.EventQueue.invokeLater (new Runnable () {
            public void run () {
                new ModelRunner ().setVisible (true);
            }
        });
    }

    private ModelRunner () {
        setTitle ("PowerTools model runner");
        setDefaultCloseOperation (WindowConstants.EXIT_ON_CLOSE);

        createSharedComponents ();
        layoutComponents ();
        pack ();

        mEndNodeRadioButton.doClick ();
        mRandomRadioButton.doClick ();
    }

    private void createSharedComponents () {
        mModelFileLabel = new JLabel ("Model file");

        mModelFileField = new JTextField (40);
        mModelFileField.getDocument ().addDocumentListener (this);

        mBrowseButton = new JButton ("Browse ...");
        mBrowseButton.addActionListener (this);

        mFileChooser = new JFileChooser ();

        mStopConditionLabel = new JLabel ("Stop condition");

        mEndNodeRadioButton = new JRadioButton ("End node");
        mEndNodeRadioButton.addActionListener (this);

        mAllNodesRadioButton = new JRadioButton ("All nodes");
        mAllNodesRadioButton.addActionListener (this);

        mAllEdgesRadioButton = new JRadioButton ("All edges");
        mAllEdgesRadioButton.addActionListener (this);

        mNeverRadioButton = new JRadioButton ("Never");
        mNeverRadioButton.addActionListener (this);

        ButtonGroup stopConditionGroup = new ButtonGroup ();
        stopConditionGroup.add (mEndNodeRadioButton);
        stopConditionGroup.add (mAllNodesRadioButton);
        stopConditionGroup.add (mAllEdgesRadioButton);
        stopConditionGroup.add (mNeverRadioButton);

        mSelectorLabel = new JLabel ("Edge selector");

        mRandomRadioButton = new JRadioButton ("Random");
        mRandomRadioButton.addActionListener (this);
        
        mWeightedRadioButton = new JRadioButton ("Weighted");
        mWeightedRadioButton.addActionListener (this);

        ButtonGroup selectorGroup = new ButtonGroup ();
        selectorGroup.add (mRandomRadioButton);
        selectorGroup.add (mWeightedRadioButton);

        mGoButton = new JButton ("Go");
        mGoButton.setEnabled (false);
        mGoButton.addActionListener (this);
    }

    private void layoutComponents () {
        GroupLayout layout = new GroupLayout (getContentPane ());
        getContentPane ().setLayout (layout);
        layout.setAutoCreateGaps (true);
        layout.setAutoCreateContainerGaps (true);
        layout.setHorizontalGroup (
            layout.createSequentialGroup ()
                .addGroup (layout.createParallelGroup ()
                    .addComponent (mModelFileLabel)
                    .addComponent (mStopConditionLabel)
                    .addComponent (mSelectorLabel))
                .addGroup (layout.createParallelGroup ()
                    .addComponent (mModelFileField)
                    .addComponent (mEndNodeRadioButton)
                    .addComponent (mAllNodesRadioButton)
                    .addComponent (mAllEdgesRadioButton)
                    .addComponent (mNeverRadioButton)
                    .addComponent (mRandomRadioButton)
                    .addComponent (mWeightedRadioButton))
                .addGroup (layout.createParallelGroup ()
                    .addComponent (mBrowseButton)
                    .addComponent (mGoButton)));

        layout.setVerticalGroup (
            layout.createSequentialGroup ()
                .addGroup (layout.createParallelGroup ()
                    .addComponent (mModelFileLabel)
                    .addComponent (mModelFileField)
                    .addComponent (mBrowseButton))
                .addGroup (layout.createParallelGroup ()
                    .addComponent (mStopConditionLabel)
                    .addGroup (layout.createSequentialGroup ()
                        .addComponent (mEndNodeRadioButton)
                        .addComponent (mAllNodesRadioButton)
                        .addComponent (mAllEdgesRadioButton)
                        .addComponent (mNeverRadioButton)))
                .addGroup (layout.createParallelGroup ()
                    .addComponent (mSelectorLabel)
                    .addGroup (layout.createSequentialGroup ()
                        .addComponent (mRandomRadioButton)
                        .addComponent (mWeightedRadioButton))
                    .addComponent (mGoButton, GroupLayout.Alignment.TRAILING)));

        layout.linkSize (SwingConstants.HORIZONTAL, new Component[] { mBrowseButton, mGoButton });
    }

    public void actionPerformed (ActionEvent e) {
        Object source = e.getSource ();
        if (source == mBrowseButton) {
            if (mFileChooser.showOpenDialog (this) == JFileChooser.APPROVE_OPTION) {
                try {
                    File file = mFileChooser.getSelectedFile ();
                    mModelFileField.setText (file.getCanonicalPath ());
                } catch (IOException ioe) {
                    // ignore
                }
            }
        } else if (source == mEndNodeRadioButton) {
            mStopCondition = "end node";
        } else if (source == mAllNodesRadioButton) {
            mStopCondition = "all nodes";
        } else if (source == mAllEdgesRadioButton) {
            mStopCondition = "all edges";
        } else if (source == mNeverRadioButton) {
            mStopCondition = "never";
        } else if (source == mRandomRadioButton) {
            mSelector = "random";
        } else if (source == mWeightedRadioButton) {
            mSelector = "weighted";
        } else if (source == mGoButton) {
            mGoButton.setEnabled (false);
            runModel ();
            mGoButton.setEnabled (true);
            return;
        }
        enableGoButton ();
    }
    
    public void insertUpdate (DocumentEvent e) {
        enableGoButton ();
    }

    public void removeUpdate (DocumentEvent e) {
        enableGoButton ();
    }

    public void changedUpdate (DocumentEvent e) {
        enableGoButton ();
    }

    private void enableGoButton () {
        boolean allInputOk = !mModelFileField.getText ().isEmpty ()
                          && mStopCondition != null
                          && mSelector != null;
        mGoButton.setEnabled (allInputOk);
    }
    
    private void runModel () {
        File file = new File (mModelFileField.getText ());
        if (!file.exists () || !file.isFile ()) {
            reportError ("file does not exist or is a directory");
        } else {
            new ModelBasedEngine (file.getParent ()).run (file.getParent (), file.getName (), mSelector, mStopCondition);
        }
    }
    
    private void reportError (String message) {
        // TODO: show message to user
    }
}
