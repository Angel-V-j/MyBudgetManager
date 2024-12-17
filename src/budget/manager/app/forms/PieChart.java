package budget.manager.app.forms;

import budget.manager.app.controllers.CategoryController;
import budget.manager.app.controllers.TransactionController;
import budget.manager.app.managers.SessionManager;
import budget.manager.app.models.Category;
import budget.manager.app.models.Transaction;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PieChart extends JFrame {

    private JPanel jMainPanel;

    public PieChart(List<Category> categories) {
        setTitle("Expense Chart");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);

        jMainPanel = new JPanel(new GridBagLayout());


        setContentPane(jMainPanel);
        loadChart(categories);
        setVisible(true);
    }

    private void loadChart(List<Category> categories) {

        JPanel chartPanel = createChartPanel(categories);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;


        jMainPanel.add(chartPanel, gbc);
        jMainPanel.revalidate();
        jMainPanel.repaint();
    }

    private JPanel createChartPanel(List<Category> categories) {
        JPanel chartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


                // логика за диаграмата
                Color[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE, Color.MAGENTA};
                int startAngle = 0;
                int totalExpenses = 100; // реални данни
                double[] expenses = sortByCategories();

                List<String> categoryName = new ArrayList<>();

                for(Category category : categories){
                    categoryName.add(category.getName());
                }
                categoryName.add("Expenses");
                categoryName.add("Category");

                for (int i = 0; i < expenses.length; i++) {
                    int arcAngle = (int) Math.round((expenses[i] / (double) totalExpenses) * 360);
                    g.setColor(colors[i % colors.length]);
                    g.fillArc(100, 50, 300, 300, startAngle, arcAngle);

                    // Легенда
                    g.setColor(Color.BLACK);
                    g.drawString(categoryName.get(i)  + ": " + expenses[i] + "%",
                            450, 100 + i * 20);

                    startAngle += arcAngle;
                }
            }
        };

        chartPanel.setPreferredSize(new Dimension(500, 400));
        return chartPanel;
    }
        private double[] sortByCategories(){
            double[] doubles = new double[SessionManager.getInstance().getUserCategories().size()];
            for (int i = 0; i < doubles.length; i++) {
                for (Transaction trans : SessionManager.getInstance().getUserTransactions()) {
                    if (trans.getCategoryId() == i) {
                        doubles[i]++;
                    }
                }
            }

            for (int i = 0; i < doubles.length; i++) {
                doubles[i] = Math.round(doubles[i]/SessionManager.getInstance().getUserTransactions().size() * 100);


            }
            return doubles;
        }
}
