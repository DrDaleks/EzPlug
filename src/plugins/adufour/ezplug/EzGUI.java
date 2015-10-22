package plugins.adufour.ezplug;

import icy.gui.component.button.IcyButton;
import icy.network.NetworkUtil;
import icy.resource.ResourceUtil;
import icy.resource.icon.IcyIcon;
import icy.system.thread.ThreadUtil;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.filechooser.FileSystemView;

import plugins.adufour.vars.lang.Var;
import plugins.adufour.vars.lang.VarDouble;
import plugins.adufour.vars.util.VarListener;

public class EzGUI extends EzDialog implements ActionListener
{
    public static final int LOGO_HEIGHT = 32;
    
    private EzPlug ezPlug;
    
    private Thread executionThread;
    
    private JPanel jPanelBottom;
    
    private JPanel jPanelButtons;
    
    private JButton jButtonRun;
    
    private JButton jButtonStop;
    
    private JButton jButtonSaveParameters;
    
    private JButton jButtonLoadParameters;
    
    private JButton jButtonHelp;
    
    private boolean jButtonsParametersVisible = true;
    
    private JProgressBar jProgressBar;
    
    private final VarListener<Double> statusProgressListener = new VarListener<Double>()
    {
        @Override
        public void valueChanged(Var<Double> source, Double oldValue, final Double newValue)
        {
            ThreadUtil.invokeLater(new Runnable()
            {
                public void run()
                {
                    jButtonRun.setEnabled(newValue == 0);
                    if (ezPlug instanceof EzStoppable) jButtonStop.setEnabled(newValue != 0);
                    
                    if (!Double.isNaN(newValue)) jProgressBar.setValue((int) (Math.max(0, Math.min(1.0, newValue)) * 100));
                    jProgressBar.setIndeterminate(Double.isNaN(newValue));
                }
            });
        }
        
        @Override
        public void referenceChanged(Var<Double> source, Var<? extends Double> oldReference, Var<? extends Double> newReference)
        {
        
        }
    };
    
    private final VarListener<String> statusMessageListener = new VarListener<String>()
    {
        @Override
        public void valueChanged(Var<String> source, String oldValue, final String newValue)
        {
            ThreadUtil.invokeLater(new Runnable()
            {
                public void run()
                {
                    jProgressBar.setString(newValue);
                    jProgressBar.setStringPainted(!newValue.trim().isEmpty());
                }
            });
        }
        
        @Override
        public void referenceChanged(Var<String> source, Var<? extends String> oldReference, Var<? extends String> newReference)
        {
        
        }
    };
    
    public EzGUI(final EzPlug ezPlug)
    {
        super(ezPlug.getName(), false);
        
        this.ezPlug = ezPlug;
        
        jPanelBottom = new JPanel(new GridLayout(2, 1));
        
        jPanelButtons = new JPanel(new GridLayout(1, 5));
        jPanelBottom.add(jPanelButtons);
        
        jButtonRun = new IcyButton(new IcyIcon(ResourceUtil.getAlphaIconAsImage("playback_play.png")));
        jButtonRun.setToolTipText("Start the plug-in...");
        jButtonRun.addActionListener(this);
        jPanelButtons.add(jButtonRun);
        
        if (ezPlug instanceof EzStoppable)
        {
            jButtonStop = new IcyButton(new IcyIcon(ResourceUtil.getAlphaIconAsImage("playback_stop.png")));
            jButtonStop.setToolTipText("Stop the plug-in...");
            jButtonStop.setEnabled(false);
            jButtonStop.addActionListener(this);
            jPanelButtons.add(jButtonStop);
        }
        
        jButtonSaveParameters = new IcyButton(new IcyIcon(ResourceUtil.ICON_SAVE));
        jButtonSaveParameters.setToolTipText("Save the parameters to a file...");
        jButtonSaveParameters.addActionListener(this);
        jPanelButtons.add(jButtonSaveParameters);
        
        jButtonLoadParameters = new IcyButton(new IcyIcon(ResourceUtil.ICON_LOAD));
        jButtonLoadParameters.setToolTipText("Load the parameters from a file...");
        jButtonLoadParameters.addActionListener(this);
        jPanelButtons.add(jButtonLoadParameters);
        
        jButtonHelp = new IcyButton(new IcyIcon(ResourceUtil.ICON_HELP));
        jButtonHelp.setToolTipText("Access the online help for this plug-in...");
        jButtonHelp.addActionListener(this);
        jPanelButtons.add(jButtonHelp);
        
        jProgressBar = new JProgressBar();
        jProgressBar.setString("Running...");
        jPanelBottom.add(jProgressBar);
        
        ezPlug.getStatus().addProgressListener(statusProgressListener);
        ezPlug.getStatus().addMessageListener(statusMessageListener);
        
        getContentPane().add(jPanelBottom, BorderLayout.SOUTH);
        
        pack();
        
        setOptimalLocation();
    }
    
    /**
     * Sets the state of the "Run" button on the interface
     */
    public void setRunButtonEnabled(final boolean runnable)
    {
        ThreadUtil.invokeLater(new Runnable()
        {
            public void run()
            {
                jButtonRun.setEnabled(runnable);
            }
        });
    }
    
    /**
     * Sets the text of the run button
     */
    public void setRunButtonText(final String text)
    {
        ThreadUtil.invokeLater(new Runnable()
        {
            public void run()
            {
                jButtonRun.setText(text);
            }
        });
    }
    
    /**
     * Sets the text of the run button
     */
    public void setStopButtonText(final String text)
    {
        ThreadUtil.invokeLater(new Runnable()
        {
            public void run()
            {
                jButtonStop.setText(text);
            }
        });
    }
    
    /**
     * Sets the running state of this plug-in. The running state is materialized by enabling or
     * disabling the "Run" (and "Stop") buttons and animating the progress bar.
     * 
     * @deprecated The running state is now automatically handled via the
     *             {@link #setProgressBarValue(double)} and {@link #setProgressBarMessage(String)}
     *             methods
     * @param running
     */
    public void setRunningState(final boolean running)
    {
        ezPlug.getStatus().setCompletion(running ? Double.NaN : 0.0);
    }
    
    /**
     * Sets whether the action panel (buttons and progress bar) are visible or not
     * 
     * @param visible
     *            the new visibility state of the action panel
     */
    public void setActionPanelVisible(final boolean visible)
    {
        ThreadUtil.invokeLater(new Runnable()
        {
            public void run()
            {
                jPanelBottom.setVisible(visible);
            }
        });
    }
    
    /**
     * Returns the variable used by the interface's progress bar. Any change to this variable will
     * automatically affect the corresponding progress bar
     * 
     * @return the variable controlling the progress bar
     */
    public VarDouble getProgressBarValue()
    {
        return ezPlug.getStatus().getProgressVariable();
    }
    
    public void setProgressBarMessage(final String string)
    {
        ezPlug.getStatus().setMessage(string);
    }
    
    /**
     * Sets the progress indicator for this plug-in
     * 
     * @param value
     *            <ul>
     *            <li>NaN: no animation (default)</li>
     *            <li>0: started (infinitely running)</li>
     *            <li>from 0 to 1: a progress percentage indicator</li>
     *            </ul>
     */
    public void setProgressBarValue(final double value)
    {
        ezPlug.getStatus().setCompletion(value);
    }
    
    public void setProgressBarVisible(final boolean visible)
    {
        ThreadUtil.invokeLater(new Runnable()
        {
            public void run()
            {
                jProgressBar.setVisible(visible);
            }
        });
    }
    
    public void setParametersIOVisible(final boolean visible)
    {
        if (visible == jButtonsParametersVisible) return;
        
        ThreadUtil.invokeLater(new Runnable()
        {
            public void run()
            {
                if (visible)
                {
                    jPanelButtons.add(jButtonLoadParameters);
                    jPanelButtons.add(jButtonSaveParameters);
                }
                else
                {
                    jPanelButtons.remove(jButtonLoadParameters);
                    jPanelButtons.remove(jButtonSaveParameters);
                }
            }
        });
        
        jButtonsParametersVisible = visible;
    }
    
    /**
     * Simulates a click on the run button (useful to execute the plug-in right after
     * initialization)
     */
    public void clickRun()
    {
        executionThread = new Thread(ezPlug, ezPlug.getName());
        executionThread.start();
    }
    
    // ActionListener //
    
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource().equals(jButtonRun))
        {
            clickRun();
        }
        else if (e.getSource().equals(jButtonStop))
        {
            if (ezPlug instanceof EzStoppable) ((EzStoppable) ezPlug).stopExecution();
        }
        else if (e.getSource().equals(jButtonLoadParameters))
        {
            JFileChooser jfc = currentParametersPath == null ? new JFileChooser(FileSystemView.getFileSystemView()) : new JFileChooser(currentParametersPath);
            
            if (jfc.showOpenDialog(getContentPane()) != JFileChooser.APPROVE_OPTION) return;
            
            currentParametersPath = jfc.getCurrentDirectory();
            
            ezPlug.loadParameters(jfc.getSelectedFile());
            
        }
        else if (e.getSource().equals(jButtonSaveParameters))
        {
            JFileChooser jfc = currentParametersPath == null ? new JFileChooser(FileSystemView.getFileSystemView()) : new JFileChooser(currentParametersPath);
            
            if (jfc.showSaveDialog(getContentPane()) != JFileChooser.APPROVE_OPTION) return;
            
            currentParametersPath = jfc.getCurrentDirectory();
            
            ezPlug.saveParameters(jfc.getSelectedFile());
        }
        else if (e.getSource().equals(jButtonHelp))
        {
            NetworkUtil.openBrowser(ezPlug.getDescriptor().getWeb() + "#documentation");
        }
        else
        {
            throw new UnsupportedOperationException("Action event not recognized for source " + e.getSource());
        }
    }
    
    @SuppressWarnings("deprecation")
    public void onClosed()
    {
        super.onClosed();
        
        if (ezPlug == null) return;
        
        if (executionThread != null && executionThread.isAlive())
        {
            ezPlug.stopExecution();
            
            // stop the execution if it was still running
            if (!(ezPlug instanceof EzStoppable))
            {
                // special case: process needs to be force-killed.
                // => use the dedicated interruption handler
                executionThread.setUncaughtExceptionHandler(new EzStoppable.ForcedInterruptionHandler(ezPlug.getDescriptor()));
                executionThread.stop();
            }
        }
        
        ezPlug.cleanFromUI();
        
        // remove all listeners
        
        jButtonRun.removeActionListener(this);
        if (jButtonStop != null) jButtonStop.removeActionListener(this);
        jButtonLoadParameters.removeActionListener(this);
        jButtonSaveParameters.removeActionListener(this);
        ezPlug.getStatus().removeProgressListener(statusProgressListener);
        ezPlug.getStatus().removeMessageListener(statusMessageListener);
        
        ezPlug = null;
    }
    
}
