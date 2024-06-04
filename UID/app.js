const btn = document.querySelector(".fa")
btn.addEventListener("click",function show(){
    const searchbox = document.getElementById("search-item")
    searchbox.style.display = 'block';
    btn.style.display = 'none';
})

function search() {
    const searchbox = document.getElementById("search-item").value.toLowerCase();
    const cityItems = document.querySelectorAll(".items");
    const headings = document.querySelectorAll(".items h2");

    for (let i = 0; i < headings.length; i++) {
        let textValue = headings[i].innerText.toLowerCase();

        if (textValue.includes(searchbox)) {
            cityItems[i].style.display = "";
        } else {
            cityItems[i].style.display = "none";
        }
    }
}

