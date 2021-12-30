package az.zero.azcypher

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.isDigitsOnly
import androidx.core.view.isVisible
import az.zero.azcypher.cypher.*
import az.zero.azcypher.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var convertedMsg: String = ""
    private var menuCipherType = CypherType.CAESAR_CIPHER

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            encryptBtn.setOnClickListener {
                executeCipher(Operation.ENCRYPT, menuCipherType)
            }
            decryptBtn.setOnClickListener {
                executeCipher(Operation.DECRYPT, menuCipherType)
            }

        }
    }


    private fun executeCipher(operation: Operation, menuCipherType: CypherType) {
        try {
            val key = binding.keyEt.text.toString()
            if (key.isEmpty()) throw Exception("Enter a key!")
            val msg = binding.textEt.text.toString()
            if (msg.isEmpty()) throw Exception("Enter a message!")
            val cipherType = menuCipherType
            if (cipherType == CypherType.CAESAR_CIPHER && !key.isDigitsOnly())
                throw Exception("Key must be a number!")
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
        binding.textEt.setText(convertedMsg)
        binding.resultCv.isVisible = true
        binding.resultTv.text = this.convertedMsg
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.caesar_item -> {
                menuCipherType = CypherType.CAESAR_CIPHER
                binding.cipherTypeTextTv.text = "Caesar Cipher"
                true
            }
            R.id.fair_play_item -> {
                menuCipherType = CypherType.PLAYFAIR_CIPHER
                binding.cipherTypeTextTv.text = "Fairplay Cipher"
                true
            }
            R.id.transposition_item -> {
                menuCipherType = CypherType.TRANSPOSITION_CIPHER
                binding.cipherTypeTextTv.text = "Transposition Cipher"
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}