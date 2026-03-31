// ===============================
// GLOBAL VARIABLES
// ===============================
let user = JSON.parse(localStorage.getItem("user"));
let remindersShown = false;
let deleteTaskId = null;

const API = "https://study-planner-467z.onrender.com/tasks";


// ===============================
// PAGE LOAD
// ===============================
window.onload = function(){

    // Dark mode load
    if(localStorage.getItem("darkMode") === "true"){
        document.body.classList.add("dark");
    }

    // Check login
    if(!user){
        window.location = "index.html";
        return;
    }

    // Welcome message
    const welcome = document.getElementById("welcome");
    if(welcome){
        welcome.innerText = "Welcome, " + user.name;
    }

    loadTasks();
};


// ===============================
// LOAD TASKS
// ===============================
function loadTasks(){

    fetch(API + "/" + user.id)
    .then(res => {
        if(!res.ok){
            throw new Error("Server error");
        }
        return res.json();
    })
    .then(data => {

        let search = document.getElementById("search").value.toLowerCase();
        let filter = document.getElementById("filter").value;

        let today = new Date();
        today.setHours(0,0,0,0);

        // Filter + search
        let tasks = data.filter(t => {

            let matchSearch =
                t.task.toLowerCase().includes(search) ||
                t.subject.toLowerCase().includes(search);

            let matchFilter =
                filter === "all" ||
                (filter === "completed" && t.completed) ||
                (filter === "pending" && !t.completed);

            return matchSearch && matchFilter;
        });

        // Smart sort
        tasks.sort((a,b)=>{

            let da = new Date(a.deadline);
            let db = new Date(b.deadline);

            if(a.completed !== b.completed){
                return a.completed - b.completed;
            }

            return da - db;
        });

        let list = document.getElementById("taskList");
        list.innerHTML = "";

        tasks.forEach(t => {

            let deadline = new Date(t.deadline);
            deadline.setHours(0,0,0,0);

            let diffDays = Math.ceil((deadline - today)/(1000*60*60*24));

            let card = document.createElement("div");
            card.className = "taskCard";

            // Deadline color
            if(diffDays < 0){
                card.style.borderLeft = "6px solid red";
            }
            else if(diffDays === 0){
                card.style.borderLeft = "6px solid orange";
            }
            else{
                card.style.borderLeft = "6px solid green";
            }

            if(t.completed){
                card.style.opacity = "0.6";
            }

            card.innerHTML = `
<div class="taskSubject">${t.subject}</div>

<div class="taskMiddle">
    <div class="taskText">${t.task}</div>
    <div class="taskDate">${t.deadline}</div>
</div>

<div class="taskButtons">

<button onclick='editTask(${t.id},"${t.subject}","${t.task}","${t.deadline}")'>
Edit
</button>

${t.completed
? "<button disabled>Done</button>"
: `<button onclick="completeTask(${t.id})">Complete</button>`
}

<button onclick="openDeletePopup(${t.id})">
Delete
</button>

</div>
`;

            list.appendChild(card);
        });

        // Progress calculation
        let total = data.length;
        let completed = data.filter(t => t.completed).length;

        let percent = 0;

        if(total > 0){
            percent = Math.round((completed / total) * 100);
        }

        document.getElementById("progressFill").style.width = percent + "%";
        document.getElementById("progressText").innerText = percent + "% completed";

        updateMotivation(percent);

        // Reminder notifications
        if(!remindersShown){
            setTimeout(()=>{
                checkReminders(data);
                remindersShown = true;
            },500);
        }

    })
    .catch(err=>{
        console.error("Task loading error:",err);
    });
}


// ===============================
// ADD TASK
// ===============================
function addTask(){

    let subjectVal = document.getElementById("subject").value.trim();
    let taskVal = document.getElementById("task").value.trim();
    let deadlineVal = document.getElementById("deadline").value;

    if(subjectVal === "" || taskVal === "" || deadlineVal === ""){
        alert("Please fill all fields!");
        return;
    }

    fetch(API,{
        method:"POST",
        headers:{"Content-Type":"application/json"},
        body: JSON.stringify({
            subject: subjectVal,
            task: taskVal,
            deadline: deadlineVal,
            completed:false,
            userId:user.id
        })
    })
    .then(res => res.json())
    .then(()=>{
        document.getElementById("subject").value="";
        document.getElementById("task").value="";
        document.getElementById("deadline").value="";

        loadTasks();
        showToast("Task added successfully ✅","green");
    });
}


// ===============================
// DELETE TASK
// ===============================
function deleteTask(id){

    fetch(API+"/"+id,{method:"DELETE"})
    .then(()=>{
        loadTasks();
        showToast("Task deleted 🗑️","red");
    });
}


// ===============================
// COMPLETE TASK
// ===============================
function completeTask(id){

    fetch(API+"/"+id+"/complete",{method:"PUT"})
    .then(()=>{
        loadTasks();
        showToast("Task completed 🎉","orange");
    });
}


// ===============================
// EDIT TASK
// ===============================
function editTask(id,oldSub,oldTask,oldDate){

    let subject = prompt("Edit subject",oldSub);
    let task = prompt("Edit task",oldTask);
    let deadline = prompt("Edit date (YYYY-MM-DD)",oldDate);

    fetch(API + "/" + id,{
        method:"PUT",
        headers:{"Content-Type":"application/json"},
        body:JSON.stringify({subject,task,deadline})
    })
    .then(()=>{
        loadTasks();
        showToast("Task updated ✏️","#4f46e5");
    });
}


// ===============================
// MOTIVATION QUOTES
// ===============================
function updateMotivation(percent){

let quote="";

if(percent === 0){
quote="Start small. Every task counts 🚀";
}
else if(percent < 30){
quote="Good start! Keep going 💪";
}
else if(percent < 60){
quote="You're making solid progress 📚";
}
else if(percent < 90){
quote="Almost there! Stay focused 🔥";
}
else if(percent === 100){
quote="Amazing work! All tasks completed 🎉";
}
else{
quote="Just a few more to finish ⭐";
}

document.getElementById("progressQuote").innerText = quote;
}


// ===============================
// REMINDERS
// ===============================
function checkReminders(tasks){

    let today=new Date();
    today.setHours(0,0,0,0);

    tasks.forEach((t,index)=>{

        if(t.completed) return;

        let deadline=new Date(t.deadline);
        deadline.setHours(0,0,0,0);

        let diffDays=Math.ceil((deadline-today)/(1000*60*60*24));

        let message="";
        let color="#333";

        if(diffDays<0){
            message=`⚠ ${t.task} is OVERDUE`;
            color="red";
        }
        else if(diffDays===0){
            message=`⚡ ${t.task} is due TODAY`;
            color="orange";
        }
        else if(diffDays<=2){
            message=`⏳ ${t.task} due in ${diffDays} day(s)`;
            color="#4f46e5";
        }

        if(message){
            setTimeout(()=>{
                showToast(message,color);
            },index*800);
        }

    });
}


// ===============================
// TOAST NOTIFICATIONS
// ===============================
function showToast(message,color="#333"){

    let container=document.getElementById("toastContainer");

    let toast=document.createElement("div");
    toast.className="toast";
    toast.innerText=message;
    toast.style.background=color;

    container.appendChild(toast);

    setTimeout(()=>{
        toast.classList.add("show");
    },100);

    setTimeout(()=>{
        toast.classList.remove("show");
        setTimeout(()=>toast.remove(),400);
    },3000);
}


// ===============================
// DELETE POPUP
// ===============================
function openDeletePopup(id){
    deleteTaskId=id;
    document.getElementById("deletePopup").classList.add("show");
}

function closeDeletePopup(){
    document.getElementById("deletePopup").classList.remove("show");
}

function confirmDelete(){
    deleteTask(deleteTaskId);
    closeDeletePopup();
}


// ===============================
// DARK MODE
// ===============================
function toggleDarkMode(){

document.body.classList.toggle("dark");

if(document.body.classList.contains("dark")){
localStorage.setItem("darkMode","true");
}
else{
localStorage.setItem("darkMode","false");
}

}


// ===============================
// NAVIGATION
// ===============================
function logout(){
localStorage.removeItem("user");
window.location="index.html";
}

function goDashboard(){
window.location="main-dashboard.html";
}

function goProfile(){
window.location="profile.html";
}