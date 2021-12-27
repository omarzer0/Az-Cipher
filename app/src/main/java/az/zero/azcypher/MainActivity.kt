package az.zero.azcypher

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import az.zero.azcypher.cypher.*
import az.zero.azcypher.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var convertedMsg: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            encryptBtn.setOnClickListener {
//                execute(Operation.ENCRYPT)
                executeCipher(Operation.ENCRYPT)
            }
            decryptBtn.setOnClickListener {
//                execute(Operation.DECRYPT)
                executeCipher(Operation.DECRYPT)
            }

            copyBtn.setOnClickListener {
                convertedMsg = resultTv.text.toString()
                Toast.makeText(this@MainActivity, "Copied!", Toast.LENGTH_SHORT).show()
            }

            pasteBtn.setOnClickListener {
                textEt.setText(convertedMsg)
            }
        }
    }


    private fun executeCipher(operation: Operation) {
        try {
            val key = binding.keyEt.text.toString()
            if (key.isEmpty()) throw Exception("Enter a key!")
            val msg = binding.textEt.text.toString()
            if (msg.isEmpty()) throw Exception("Enter a message!")
            val cipherType = getCypherType()
            val cipher: Cipher = getCipher(cipherType, key)

            convertedMsg = when (operation) {
                Operation.ENCRYPT -> cipher.encrypt(msg)
                Operation.DECRYPT -> cipher.decrypt(msg)
            }
            updateUI()
        } catch (e: Exception) {
            Log.e("TAG", "error: ${e.localizedMessage ?: "Unknown"}")
            Toast.makeText(this, e.localizedMessage ?: "Unknown", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getCypherType(): CypherType = when (binding.radioGroup.checkedRadioButtonId) {
        R.id.caesar_rbtn -> {
            CypherType.CAESAR_CIPHER
        }
        R.id.playfair_rbtn -> {
            CypherType.PLAYFAIR_CIPHER
        }
        else -> {
            CypherType.TRANSPOSITION_CIPHER
        }
    }

    private fun getCipher(cypherType: CypherType, key: String) = when (cypherType) {
        CypherType.CAESAR_CIPHER -> {
            CaesarCipher(Integer.valueOf(key))
        }
        CypherType.PLAYFAIR_CIPHER -> {
            PlayfairCipher(key)
        }
        CypherType.TRANSPOSITION_CIPHER -> {
            TranspositionCipher(key)
        }
    }

    private fun updateUI() {
        binding.resultTv.isVisible = true
        binding.resultTv.text = this.convertedMsg
    }
}