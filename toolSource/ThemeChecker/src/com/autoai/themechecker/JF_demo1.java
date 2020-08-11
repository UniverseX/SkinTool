package com.autoai.themechecker;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class JF_demo1 extends JApplet {
    private JEditorPane dstPathEditPanel;
    private OnClickListener onClickListener;
    private String dstPath;
    private GridBagLayout gb_child2;
    private GridBagLayout gb_child3;
    private GridBagLayout gb_child4;
    private JPanel panel1;
    private JPanel panel2;
    private JPanel panel3;
    private JPanel panel4;
    private JList<String> standList;
    private JList<String> dstList;

    public JF_demo1() {
    }

    public void initPanel() {
        GridBagLayout parent = new GridBagLayout();
        setLayout(parent);

        GridBagLayout gb_child1 = new GridBagLayout();
        gb_child2 = new GridBagLayout();
        gb_child3 = new GridBagLayout();
        gb_child4 = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.gridwidth = GridBagConstraints.REMAINDER;
        panel1 = makePanel(this, parent, gb_child1, c);
        c.gridwidth = GridBagConstraints.REMAINDER;
        panel2 = makePanel(this, parent, gb_child2, c);
        c.gridwidth = GridBagConstraints.REMAINDER;
        panel3 = makePanel(this, parent, gb_child3, c);
        c.gridwidth = GridBagConstraints.RELATIVE;
        panel4 = makePanel(this, parent, gb_child4, c);


        GridBagConstraints cc1 = new GridBagConstraints();

        cc1.fill = GridBagConstraints.BOTH;
        cc1.weightx = 0;
        cc1.gridwidth = 1;
        makeTitleLabel(panel1, "请输入主题路径：", gb_child1, cc1);
        cc1.weightx = 1;
        cc1.gridwidth = GridBagConstraints.REMAINDER;
        dstPathEditPanel = makeEditLabel(panel1, dstPath, gb_child1, cc1);

        GridBagConstraints cc2 = new GridBagConstraints();
        cc2.gridx = 1;
        cc2.weightx = 1;

        makeButtonLabel(panel2, "开始检查", gb_child2, cc2, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                dstPath = dstPathEditPanel.getText();
                File file = new File(dstPath);
                if(!file.exists()) {
                    JOptionPane.showMessageDialog(panel1, "路径不存在");
                }else {
                    if (onClickListener != null) {
                        onClickListener.onClick(dstPath);
                    }
                }
            }
        });
        makeTitleLabel(panel2, "", gb_child2, cc2);

        //lazy load
//        showResult();
    }
    
    public void showResult(String[] standardDiff, String[] dstDiff) {

//        GridBagConstraints cc3 = new GridBagConstraints();
//        cc3.gridwidth = GridBagConstraints.REMAINDER;
//        makeTitleLabel(panel3, "目标主题路径：" + dstPath, gb_child3, cc3);

        if(standList == null) {
            GridBagConstraints cc4 = new GridBagConstraints();
            cc4.weightx = 1;
            makeTitleLabel(panel4, "标准主题未匹配文件", gb_child4, cc4);
            cc4.gridwidth = GridBagConstraints.REMAINDER;
            makeTitleLabel(panel4, "目标主题多余文件", gb_child4, cc4);

            cc4.gridwidth = 1;                //reset to the default
            cc4.weighty = 1.0;
            standList = makeListPanel(panel4, standardDiff, gb_child4, cc4);
            dstList = makeListPanel(panel4, dstDiff, gb_child4, cc4);
        }else {
            standList.setListData(standardDiff);
            dstList.setListData(dstDiff);
        }
    }


    public static void initNoData(String reason) {
        JFrame f = new JFrame("主题路径检查");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setMinimumSize(new Dimension(800, 400));


        JF_demo1 jf_demo1 = new JF_demo1();
        jf_demo1.initNoDataPanel(reason);

        f.add("Center", jf_demo1);
        f.setSize(f.getPreferredSize());
        f.setVisible(true);
        f.pack();
    }

    public void initNoDataPanel(String reason) {
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();

        setLayout(gridbag);

        c.fill = GridBagConstraints.CENTER;
        makeTitleLabel(this, reason, gridbag, c);
    }

    private void makeButtonLabel(final Container prarent, String title, GridBagLayout gridbag,
                                 GridBagConstraints c, ActionListener actionListener) {
        final JButton jButton = new JButton(title);
        jButton.setFont(new Font("SansSerif", Font.PLAIN, 20));
        if(actionListener != null){
            jButton.addActionListener(actionListener);
        }
        gridbag.setConstraints(jButton, c);
        prarent.add(jButton);
    }

    private JEditorPane makeEditLabel(Container prarent, String path, GridBagLayout gridbag,
                               GridBagConstraints c) {
        JEditorPane dstPathEditPanel = new JEditorPane();
        dstPathEditPanel.setText(path);
        gridbag.setConstraints(dstPathEditPanel, c);
        prarent.add(dstPathEditPanel);
        return dstPathEditPanel;
    }

    private JLabel makeTitleLabel(Container prarent, String title, GridBagLayout gridbag,
                                GridBagConstraints c) {
        JLabel jLabel = new JLabel(title);
        jLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));
        gridbag.setConstraints(jLabel, c);
        prarent.add(jLabel);
        return jLabel;
    }

    private JList<String> makeListPanel(Container prarent, String[] data, GridBagLayout gridbag,
                               GridBagConstraints c) {
        JList<String> myList = new JList<>(data);
        myList.setFont(new Font("SansSerif", Font.PLAIN, 18));
        JScrollPane scrollPaneLeft = new JScrollPane(myList);
        gridbag.setConstraints(scrollPaneLeft, c);
        prarent.add(scrollPaneLeft);
        return myList;
    }

    private JPanel makePanel(Container prarent,  GridBagLayout parentL,  GridBagLayout currentL,
                             GridBagConstraints c) {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(currentL);
        parentL.setConstraints(jPanel, c);
        add(jPanel);
        return jPanel;
    }

	public interface OnClickListener{
        void onClick(String dstPath);
    }

    public void setOnClickListenerS5(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public OnClickListener getOnClickListener() {
        return onClickListener;
    }

    public JEditorPane getDstPathEditPanel() {
        return dstPathEditPanel;
    }
}
