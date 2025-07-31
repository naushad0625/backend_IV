const editor = document.getElementById("editor");
const status = document.getElementById("status");
const saveBtn = document.getElementById("saveBtn");

function fetchContent() {
    const content = editor.value;
    return content;
}

function saveContent() {
    console.log("Saving content...");
    const content = fetchContent();
    const docId = "doc_01";

    fetch("/save", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({docId, content})
    })
        .then(response => response.text())  // ← parse as text
        .then(data => {
            console.log(data);
        });

}

saveBtn.onclick = () => {
    saveContent();
}
setInterval(() => {
    saveContent();
}, 10000)