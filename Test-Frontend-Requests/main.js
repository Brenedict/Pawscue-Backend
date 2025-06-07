const p_tag = document.getElementById("return-requests");

sendAdopter();

function sendAdopter() {
    const adopterData = {
        adopterId: null,
        adopterName: "Garcia, Juan, M.",
        contactNum: "0917-111-2222",
        emailAddress: "juan.garcia@example.com",
        addressDetails: {
            adopterAddressId: "HOME-0001",
            zipcode: "1100",
            homeAddress: "123 Main St",
            city: "Manila",
            state: "NCR",
            housingStatus: "Own",
            homePetPolicy: "ALLOWED",
            windowScreens: true,
            homeChildrenNum: 2,
            petLivingArea: "Indoors"
        },
        employmentStatus: "Working",
        workingHrs: 9,
        workContactNum: "0917-222-3333",
        employerName: "Tech Solutions",
        workAddress: "123 Main St, Manila",
        petAloneHours: 5,
        petCareTaker: "Self",
        spouse: {
            adopterId: "ADP-0001",
            workAddress: "123 Main St, Manila",
            workingHrs: 8,
            spouseId: "AS-0001",
            spouseName: "Garcia, Maria, S.",
            employerName: "Tech Solutions",
            workContactNum: "0917-333-4444"
        }
    };

    fetch("http://localhost:8080/api/adoption-record/adopter/save", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(adopterData)
    })

    .then(response => response.text())

    .then(data => {
        console.log("Success:", data);
        p_tag.textContent = data;
        alert("Adopter submitted successfully!");
    })

    .catch(error => {
        console.error("Error:", error);
        alert("Failed to submit adopter.");
    });
}