from flask import Flask, request, jsonify
from llama_index.llms.ollama import Ollama

app = Flask(__name__)
llm = Ollama(model="gemma2")

@app.route('/api/response', methods=['POST'])
def get_response():
    if request.is_json:
        data = request.get_json()
        msg = data.get("message", "")
    else:
        msg = request.data.decode('utf-8')

    response = llm.complete(msg)
    return jsonify({"response": response})

if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0', port=5000)
