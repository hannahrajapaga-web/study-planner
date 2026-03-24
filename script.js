let user = JSON.parse(localStorage.getItem("user"));
let remindersShown = false;
if(localStorage.getItem("darkMode") === "true"){
document.body.classList.add("dark");
}

if(!user){
    window.location = "index.html";
}

document.getElementById("welcome").innerText = "Welcome, " + user.name;

const API = "http://localhost:9090/tasks";

// LOAD TASKS
function loadTasks(){

    fetch(API + "/" + user.id)
    .then(res=>res.json())
    .then(data=>{

        let search = document.getElementById("search").value.toLowerCase();
        let filter = document.getElementById("filter").value;

        let today = new Date();
        today.setHours(0,0,0,0);

        // FILTER + SEARCH
        let tasks = data.filter(t=>{
            let matchSearch = t.task.toLowerCase().includes(search) || t.subject.toLowerCase().includes(search);

            let matchFilter =
                filter === "all" ||
                (filter === "completed" && t.completed) ||
                (filter === "pending" && !t.completed);

            return matchSearch && matchFilter;
        });

        // SMART SORT
        tasks.sort((a,b)=>{
            let da = new Date(a.deadline);
            let db = new Date(b.deadline);

            if(a.completed !== b.completed) return a.completed - b.completed;

            return da - db;
        });

        let list = document.getElementById("taskList");
        list.innerHTML="";

        tasks.forEach(t=>{

            let deadline = new Date(t.deadline);
            deadline.setHours(0,0,0,0);

            let diffDays = Math.ceil((deadline - today)/(1000*60*60*24));

            let card=document.createElement("div");
            card.className="taskCard";

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
                card.style.opacity="0.6";
            }

           card.innerHTML = `

<div class="taskSubject">
    ${t.subject}
</div>

<div class="taskMiddle">
    <div class="taskText">${t.task}</div>
    <div class="taskDate">${t.deadline}</div>
</div>

<div class="taskButtons">

<button onclick="editTask(${t.id},'${t.subject}','${t.task}','${t.deadline}')">
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
        let total = data.length;
let completed = data.filter(t => t.completed).length;

let percent = 0;

if(total > 0){
percent = Math.round((completed / total) * 100);
}

document.getElementById("progressFill").style.width = percent + "%";
document.getElementById("progressText").innerText = percent + "% completed";

updateMotivation(percent);

        // SHOW REMINDERS ONLY ON FIRST LOAD
        if(!remindersShown){
            setTimeout(()=>{
                checkReminders(data);
                remindersShown = true;
            },500);
        }

    });
}
function confirmDelete(id){

if(confirm("Delete this task?")){
deleteTask(id);
}

}


// ADD TASK
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
    .then(() => {

        document.getElementById("subject").value = "";
        document.getElementById("task").value = "";
        document.getElementById("deadline").value = "";

        loadTasks();
        showToast("Task added successfully ✅","green");
    });
}


// DELETE
function deleteTask(id){

    fetch(API+"/"+id,{method:"DELETE"})
    .then(()=>{
        loadTasks();
        showToast("Task deleted 🗑️","red");
    });

}


// COMPLETE
function completeTask(id){

    fetch(API+"/"+id+"/complete",{method:"PUT"})
    .then(()=>{
        loadTasks();
        showToast("Task completed 🎉","orange");
    });

}


// EDIT
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
function updateMotivation(percent){

let quote="";

if(percent == 0){
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

else if(percent == 100){
quote="Amazing work! All tasks completed 🎉";
}

else{
quote="Just a few more to finish ⭐";
}

document.getElementById("progressQuote").innerText = quote;

}


// LOGOUT
function logout(){
    localStorage.removeItem("user");
    window.location="index.html";
}


// DASHBOARD
function goDashboard(){
    window.location = "main-dashboard.html";
}


// TOAST SYSTEM
function showToast(message,color="#333"){

    let container = document.getElementById("toastContainer");

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


// REMINDERS
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


// DARK MODE
function toggleDarkMode(){

document.body.classList.toggle("dark");

if(document.body.classList.contains("dark")){
localStorage.setItem("darkMode","true");
}
else{
localStorage.setItem("darkMode","false");
}

}

// LOAD PAGE
window.onload=()=>{

    if(localStorage.getItem("dark")==="true"){
        document.body.classList.add("dark");
    }

    loadTasks();
};
let deleteTaskId = null;


function openDeletePopup(id){

deleteTaskId = id;

document.getElementById("deletePopup").classList.add("show");

}


function closeDeletePopup(){

document.getElementById("deletePopup").classList.remove("show");

}


function confirmDelete(){

deleteTask(deleteTaskId);

closeDeletePopup();

}
function goProfile(){
window.location = "profile.html";
}