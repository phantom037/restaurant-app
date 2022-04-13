 // ----------------------------------------------------
// PROPERTIES + START APP
// ----------------------------------------------------
let currentIndex = 0;
let ws;
let restaurant;
const PORT = 1234;

// Start the WebSocket
WebSocketTest();



// ----------------------------------------------------
// BUTTON CLICK ACTIONS: General data format: "action|index|amount
// ----------------------------------------------------

// BUTTON: Tip
$("#tipBtn").click(function() {
    var tipAmount = $("#tipField").val();

    // If data is valid, send
    if (!isNaN(tipAmount) && tipAmount != "") {
        $.modal.close();
        $("#tipField").val("");
        ws.send("tip|" + currentIndex + "|" + tipAmount);
    } else {
        alert("Please enter a valid number");
    }
});

// BUTTON: Order food
$("#orderBtn").click(function() {
    let orderNumber = $("#orderField").val();
    // If data is valid, send
    if (!isNaN(orderNumber) && Number.isInteger(parseFloat(orderNumber)) && parseInt(orderNumber) > 0 && parseInt(orderNumber) <= restaurant.menu.length) {
        $.modal.close();
        $("#orderField").val("");
        ws.send("order|" + currentIndex + "|" + (orderNumber-1));
    } else {
        alert("Please enter a valid order number from the menu")
    }
});

// BUTTON: Close table
$("#closeTableBtn").click(function() {
    let refund = $('#refundCheckbox').is(":checked");
    ws.send("closeTable|" + currentIndex + "|" + refund);
    $('#refundCheckbox').prop('checked', false);
    $.modal.close();
});

// BUTTON: Fire employee
$("#fireBtn").click(function() {
    ws.send("fire|" + currentIndex + "|X");
    $.modal.close();
});

// TEXT FIELD: Search edited
 $(document).on('input','#searchField',function () {
     rebuildInterface(restaurant);
 });



/**
 * Rebuilds the entire UI with new restaurant data
 * @param restaurant the updated data model
 */
function rebuildInterface() {

    // ----------------------------------------------------
    // BUILD: Available Waiter and Busy Waiter Card Lists
    // ----------------------------------------------------

    // Check if we are in search mode
    let searchText = $('#searchField').val().toLowerCase();

    let availableWaiterHTML = "";
    let busyWaiterHTML = "";

    // Loop through the waiter list
    for (let i = 0; i < restaurant["waiters"].length; i++) {
        let s = restaurant["waiters"][i];

        // If search is active and the name doesn't match, then skip this iteration
        if (searchText != "" && !s.name.toLowerCase().includes(searchText)) {
            continue;
        }

        // Add the appropriate card type
        if (s["order"].length > 0) {
            // Add a busy waiter card
            busyWaiterHTML += "<div id='" + i + "' class='waiterCard busyWaiterCard'>";
            busyWaiterHTML += "<div class='waiterCard-title'>" + s.name + "</div><div class=\"waiterCard-divider\"></div>";
            for (let f of s["order"]) {
                busyWaiterHTML += "<div class='waiterCard-food'>" + f.name + "</div>";
            }
            busyWaiterHTML += "</div>";
        } else {
            // Add an available waiter card
            availableWaiterHTML += "<div id='" + i + "' class='waiterCard availableWaiterCard'>";
            availableWaiterHTML += "<div class='waiterCard-title'>" + s["name"] + "</div></div>"
        }
    }

    // Update Card List HTML
    document.getElementById("availableWaiterCardList").innerHTML = availableWaiterHTML;
    document.getElementById("busyWaiterCardList").innerHTML = busyWaiterHTML;



    // ----------------------------------------------------
    // BUILD: Food Menu and Total Sales
    // ----------------------------------------------------
    let menuHTML = "";
    for (var i = 0; i < restaurant.menu.length; i++) {
        var f = restaurant.menu[i];
        menuHTML += '<tr>\n' +
            "        <td class='foodIndex'>#" + (i+1) + "</td>" +
            "        <td class='foodName'>" + f.name + "</td>" +
            "        <td class='foodPrice'>$" + f.price + "</td>" +
            "    </tr>"
    }
    document.getElementById("menuTable").innerHTML = menuHTML;
    document.getElementById("totalSales").innerText = "Total Sales: $" + restaurant.totalSales.toFixed(2);



    // ----------------------------------------------------
    // SHOW+UPDATE: Employee Modal
    // ----------------------------------------------------
    $(".waiterCard").click(function() {
        currentIndex = $(this).attr('id');
        var currentClass = $(this).attr('class');
        if (currentClass.includes("available")) {
            // Available card - hide close table
            document.getElementById("closeTableBtn").style.display = "none";
            document.getElementById("closeTableBtnBreak").style.display = "none";
            document.getElementById("refundEntry").style.display = "none";
        } else {
            document.getElementById("closeTableBtn").style.display = "inline-block";
            document.getElementById("closeTableBtnBreak").style.display = "inline-block";
            document.getElementById("refundEntry").style.display = "inline-block";
        }
        $("#employeeModal").modal();
        document.getElementById("employeeModal-title").innerHTML = restaurant["waiters"][currentIndex].name;
        document.getElementById("employeeModal-tips").innerHTML = "<i>Tips: $" + restaurant["waiters"][currentIndex].totalTips.toFixed(2) + "</i>";
        document.getElementById("employeeModal-salary").innerHTML = "<i>Salary: $" + restaurant["waiters"][currentIndex].salary.toFixed(2) + "</i>";
    });
}



// ----------------------------------------------------
// WEBSOCKET
// ----------------------------------------------------
function WebSocketTest() {

    if ("WebSocket" in window) {

        // Let us open a web socket
        ws = new WebSocket("ws://localhost:" + PORT + "/echo");

        ws.onmessage = function (evt) {
            restaurant = JSON.parse(evt.data);
            rebuildInterface();
        };

        ws.onclose = function () {

            // websocket is closed.
            alert("Connection is closed...");
        };
    } else {

        // The browser doesn't support WebSocket
        alert("WebSocket NOT supported by your Browser!");
    }
}