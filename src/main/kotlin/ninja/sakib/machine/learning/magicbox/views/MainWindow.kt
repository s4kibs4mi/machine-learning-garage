package ninja.sakib.machine.learning.magicbox.views

import de.daslaboratorium.machinelearning.classifier.bayes.BayesClassifier
import ninja.sakib.machine.learning.magicbox.daos.WordDao
import ninja.sakib.machine.learning.magicbox.models.Word
import java.awt.Dimension
import javax.swing.*
import kotlin.properties.Delegates

/**
 * @author Sakib Sami
 * Created on 2/13/17.
 */

class MainWindow : JFrame() {
    private var learnBtn: JButton by Delegates.notNull<JButton>()
    private var classifyBtn: JButton by Delegates.notNull<JButton>()
    private var result: JLabel by Delegates.notNull<JLabel>()

    private val bayesClassifier = BayesClassifier<String, String>()

    init {
        initComponents()
    }

    private fun initComponents() {
        title = "Magic Box"

        learnBtn = JButton()
        classifyBtn = JButton()
        result = JLabel()

        //======== this ========
        val contentPane = contentPane
        contentPane.layout = null

        //---- learnBtn ----
        learnBtn.text = "Learn"
        contentPane.add(learnBtn)
        learnBtn.setBounds(270, 130, 100, learnBtn.preferredSize.height)
        learnBtn.addActionListener {
            addNewWord()
        }

        //---- classifyBtn ----
        classifyBtn.text = "Classify"
        contentPane.add(classifyBtn)
        classifyBtn.setBounds(270, 170, 100, classifyBtn.preferredSize.height)
        classifyBtn.addActionListener {
            classify()
        }

        //---- result ----
        result.text = "None"
        contentPane.add(result)
        result.setBounds(70, 250, 500, 100)

        run { // compute preferred size
            val preferredSize = Dimension()
            for (i in 0..contentPane.componentCount - 1) {
                val bounds = contentPane.getComponent(i).bounds
                preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width)
                preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height)
            }
            val insets = contentPane.insets
            preferredSize.width += insets.right
            preferredSize.height += insets.bottom
            contentPane.minimumSize = preferredSize
            contentPane.preferredSize = preferredSize
        }
        setSize(645, 455)
        setLocationRelativeTo(owner)

        loadOldData()
    }

    private fun addNewWord() {
        val text: String = JOptionPane.showInputDialog("Enter Text :")
        if (text != null) {
            val label: String = JOptionPane.showInputDialog("Enter Label :")
            if (label != null) {

                val word = Word()
                word.label = label
                word.text = text
                WordDao.save(word)

                val tokenizeText = text.split(" ")
                bayesClassifier.learn(label, tokenizeText)

                JOptionPane.showMessageDialog(this, "Added")
            }
        }
    }

    private fun classify() {
        val text = JOptionPane.showInputDialog("Enter text to classify :")
        val result = bayesClassifier.classify(text.split(" "))

        this.result.text = "<html>Query : $text<br/>" +
                "Category : ${result.category}<br/>" +
                "Probability : ${result.probability}</html>"
    }

    private fun loadOldData() {
        for (word in WordDao.get()) {
            val sampleWord = word as Word

            println("${sampleWord.label} > ${sampleWord.text}")

            val tokenizeText = sampleWord.text.split(" ")
            bayesClassifier.learn(sampleWord.label, tokenizeText)
        }
    }
}
