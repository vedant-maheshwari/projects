const search = () => {
    const searchbox = document.getElementById("search-item").value.toUpperCase();
    const searchitem = document.getElementById("contents");
    const city = document.querySelectorAll(".items");
    const cityName = searchitem.getElementsByTagName("h2");

    for (var i = 0; i < cityName.length; i++) {
        let match = city[i].getElementsByTagName("h2")[0];

        if (match) {
            let textValue = match.textContent || match.innerHTML;

            if (textValue.toUpperCase().indexOf(searchbox) > -1) {
                city[i].style.display = "";
            } else {
                city[i].style.display = "none";
            }
        }
    }
}
