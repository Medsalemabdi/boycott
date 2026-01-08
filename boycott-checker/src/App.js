import { useState } from "react";
import "./App.css";

function App() {
    const [query, setQuery] = useState("");
    const [result, setResult] = useState(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");
    const API_URL = "http://localhost:32082";

    const checkProduct = async () => {
        if (!query.trim()) return;

        setLoading(true);
        setError("");
        setResult(null);

        try {
            const res = await fetch(`${API_URL}/api/products/check?name=${query}`);

            if (!res.ok) {
                throw new Error("Not found");
            }

            const data = await res.json();
            setResult(data);
        } catch (err) {
            setError("No matching product found");
        } finally {
            setLoading(false);
        }
    };

    // 🔹 Normalize fields (Product OR AiResult)
    const productName = result?.name || result?.productName;
    const isBoycotted = result?.boycotted;
    const alternative = result?.alternative;
    const source = result?.source || "Database";

    return (
        <div className="container">
            <div className="card">
                <h1>Boycott Checker</h1>
                <p>Check if a product is boycotted and find alternatives</p>

                <input
                    type="text"
                    placeholder="Type product name (e.g. coca, nest...)"
                    value={query}
                    onChange={(e) => setQuery(e.target.value)}
                />

                <button onClick={checkProduct} disabled={loading}>
                    {loading ? "Checking..." : "Check"}
                </button>

                {error && <p className="error">{error}</p>}

                {result && (
                    <div className={isBoycotted ? "result danger" : "result success"}>
                        <h3>{productName}</h3>

                        {isBoycotted ? (
                            <>
                                <p>❌ This product is boycotted</p>

                                {result.reason && (
                                    <p>
                                        🕊️ <b>Reason:</b> {result.reason}
                                    </p>
                                )}

                                {alternative && (
                                    <p>
                                        🇹🇳 <b>Alternative:</b> {alternative}
                                    </p>
                                )}
                            </>
                        ) : (
                            <p>✅ This product is not boycotted</p>
                        )}

                        <small style={{ opacity: 0.7 }}>
                            Source: {source}
                        </small>

                        {/* 🔹 Show raw LLM response */}
                        {result.llmResponse && (
                            <details style={{ marginTop: "10px" }}>
                                <summary>Show AI raw response</summary>
                                <pre style={{
                                    background: "#f4f4f4",
                                    padding: "10px",
                                    borderRadius: "6px",
                                    whiteSpace: "pre-wrap"
                                }}>
          {result.llmResponse}
        </pre>
                            </details>
                        )}
                    </div>
                )}

            </div>
        </div>
    );
}

export default App;
