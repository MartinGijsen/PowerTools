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

package org.powertools.engine.runners;

import java.awt.Color;
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

import org.powertools.engine.core.ModelBasedEngine;


public final class ModelRunner extends javax.swing.JFrame implements ActionListener, DocumentListener {
    private JLabel       mModelFileLabel;
    private JTextField   mModelFileField;
    private JButton      mBrowseButton;
    private JFileChooser mFileChooser;
    private JLabel       mStopConditionLabel;
    private JRadioButton mEndStateRadioButton;
    private JRadioButton mAllStatesRadioButton;
    private JRadioButton mAllTransitionsRadioButton;
    private JRadioButton mNeverRadioButton;
    private JLabel       mSelectorLabel;
    private JRadioButton mRandomRadioButton;
    private JRadioButton mWeightedRadioButton;
    private JButton      mGoAbortButton;
    private JLabel       mMessageLabel;
    private JLabel       mErrorMessageLabel;

    private String       mStopCondition;
    private String       mSelector;
    private ModelBasedEngine mEngine;


    private ModelRunner () {
        setTitle ("PowerTools model runner");
        setDefaultCloseOperation (WindowConstants.EXIT_ON_CLOSE);

        createSharedComponents ();
        layoutComponents ();
        pack ();

        mEndStateRadioButton.doClick ();
        mRandomRadioButton.doClick ();
    }


    public static void main (String[] args) {
        run ();
    }

    public static void run () {
        java.awt.EventQueue.invokeLater (new Runnable () {
            public void run () {
                new ModelRunner ().setVisible (true);
            }
        });
    }

    private void createSharedComponents () {
        mModelFileLabel = new JLabel ("Model file");

        mModelFileField = new JTextField (40);
        mModelFileField.getDocument ().addDocumentListener (this);

        mBrowseButton = new JButton ("Browse ...");
        mBrowseButton.addActionListener (this);

        mFileChooser = new JFileChooser ();

        mStopConditionLabel = new JLabel ("Stop condition");

        mEndStateRadioButton = new JRadioButton ("End state");
        mEndStateRadioButton.addActionListener (this);

        mAllStatesRadioButton = new JRadioButton ("All states");
        mAllStatesRadioButton.addActionListener (this);

        mAllTransitionsRadioButton = new JRadioButton ("All transitions");
        mAllTransitionsRadioButton.addActionListener (this);

        mNeverRadioButton = new JRadioButton ("Never");
        mNeverRadioButton.addActionListener (this);

        ButtonGroup stopConditionGroup = new ButtonGroup ();
        stopConditionGroup.add (mEndStateRadioButton);
        stopConditionGroup.add (mAllStatesRadioButton);
        stopConditionGroup.add (mAllTransitionsRadioButton);
        stopConditionGroup.add (mNeverRadioButton);

        mSelectorLabel = new JLabel ("Transition selector");

        mRandomRadioButton = new JRadioButton ("Random");
        mRandomRadioButton.addActionListener (this);
        
        mWeightedRadioButton = new JRadioButton ("Weighted");
        mWeightedRadioButton.addActionListener (this);

        ButtonGroup selectorGroup = new ButtonGroup ();
        selectorGroup.add (mRandomRadioButton);
        selectorGroup.add (mWeightedRadioButton);

        mGoAbortButton = new JButton ("Go");
        mGoAbortButton.setEnabled (false);
        mGoAbortButton.addActionListener (this);
        
        mMessageLabel = new JLabel ("Message");

        mErrorMessageLabel = new JLabel ("");
        mErrorMessageLabel.setForeground (Color.red);
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
                    .addComponent (mSelectorLabel)
                    .addComponent (mMessageLabel))
                .addGroup (layout.createParallelGroup ()
                    .addComponent (mModelFileField)
                    .addComponent (mEndStateRadioButton)
                    .addComponent (mAllStatesRadioButton)
                    .addComponent (mAllTransitionsRadioButton)
                    .addComponent (mNeverRadioButton)
                    .addComponent (mRandomRadioButton)
                    .addComponent (mWeightedRadioButton)
                    .addComponent (mErrorMessageLabel))
                .addGroup (layout.createParallelGroup ()
                    .addComponent (mBrowseButton)
                    .addComponent (mGoAbortButton)));

        layout.setVerticalGroup (
            layout.createSequentialGroup ()
                .addGroup (layout.createParallelGroup ()
                    .addComponent (mModelFileLabel)
                    .addComponent (mModelFileField)
                    .addComponent (mBrowseButton))
                .addGroup (layout.createParallelGroup ()
                    .addComponent (mStopConditionLabel)
                    .addGroup (layout.createSequentialGroup ()
                        .addComponent (mEndStateRadioButton)
                        .addComponent (mAllStatesRadioButton)
                        .addComponent (mAllTransitionsRadioButton)
                        .addComponent (mNeverRadioButton)))
                .addGroup (layout.createParallelGroup ()
                    .addComponent (mSelectorLabel)
                    .addGroup (layout.createSequentialGroup ()
                        .addComponent (mRandomRadioButton)
                        .addComponent (mWeightedRadioButton))
                    .addComponent (mGoAbortButton, GroupLayout.Alignment.TRAILING))
                .addGroup (layout.createParallelGroup ()
                    .addComponent(mMessageLabel)
                    .addComponent(mErrorMessageLabel)));

        layout.linkSize (SwingConstants.HORIZONTAL, new Component[] { mBrowseButton, mGoAbortButton });
    }

    @Override
    public void actionPerformed (ActionEvent e) {
        mErrorMessageLabel.setText ("");
        Object source = e.getSource ();
        if (source.equals (mBrowseButton)) {
            setSelectedFile ();
        } else if (source.equals (mEndStateRadioButton)) {
            mStopCondition = "end state";
        } else if (source.equals (mAllStatesRadioButton)) {
            mStopCondition = "all states";
        } else if (source.equals (mAllTransitionsRadioButton)) {
            mStopCondition = "all transitions";
        } else if (source.equals (mNeverRadioButton)) {
            mStopCondition = "never";
        } else if (source.equals (mRandomRadioButton)) {
            mSelector = "random";
        } else if (source.equals (mWeightedRadioButton)) {
            mSelector = "weighted";
        } else if (source.equals (mGoAbortButton)) {
            if ("Go".equals (mGoAbortButton.getText ())) {
                runModel ();
            } else {
                mGoAbortButton.setEnabled (false);
                mEngine.abort ();
            }
            return;
        }
        enableGoButton ();
    }

    private void setSelectedFile () {
        if (mFileChooser.showOpenDialog (this) == JFileChooser.APPROVE_OPTION) {
            try {
                File file = mFileChooser.getSelectedFile ();
                mModelFileField.setText (file.getCanonicalPath ());
            } catch (IOException ioe) {
                // ignore
            }
        }
    }

    @Override
    public void insertUpdate (DocumentEvent e) {
        enableGoButton ();
    }

    @Override
    public void removeUpdate (DocumentEvent e) {
        enableGoButton ();
    }

    @Override
    public void changedUpdate (DocumentEvent e) {
        enableGoButton ();
    }

    private void enableGoButton () {
        boolean allInputOk = !mModelFileField.getText ().isEmpty ()
                          && mStopCondition != null
                          && mSelector != null;
        mGoAbortButton.setEnabled (allInputOk);
    }
    
    private void runModel () {
        File file = new File (mModelFileField.getText ());
        if (!file.exists () || !file.isFile ()) {
            reportError ("file does not exist or is a directory");
        } else {
            new MyThread (file).start ();
        }
    }

    private void reportError (String message) {
        mErrorMessageLabel.setText (message);
    }


    private class MyThread extends Thread {
        private final File mFile;


        MyThread (File file) {
            mFile   = file;
            mEngine = new ModelBasedEngine (mFile.getParent ());
        }

        @Override
        public void run () {
            mGoAbortButton.setText ("Abort");
            mEngine.run (mFile.getParent (), mFile.getName (), mSelector, mStopCondition);
            mGoAbortButton.setText ("Go");
            mGoAbortButton.setEnabled (true);
        }
    }
}
