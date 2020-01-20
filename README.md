# JSON-Parser-Java
A JSON Parser written in Java.

### Project Structure

 - tokenizer: Code for lexical analysis (token generation)
   - tokens: Definitions for Tokens
 - value: Code for Java representation of JSON values
 - parser: Code for parsing list of generated tokens
   - Printer: Code for pretty-printing/string-conversion of JSON values

 **Note:** Sepration of tokenization and parsing is not necessary, it is done for convinience and learning purpose.
