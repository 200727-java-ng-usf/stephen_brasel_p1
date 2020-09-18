
// .___       .__  __  .__       .__  .__               
// |   | ____ |__|/  |_|__|____  |  | |__|_______ ____  
// |   |/    \|  \   __\  \__  \ |  | |  \___   // __ \ 
// |   |   |  \  ||  | |  |/ __ \|  |_|  |/    /\  ___/ 
// |___|___|  /__||__| |__(____  /____/__/_____ \\___  >
//          \/                 \/              \/    \/ 
//#region Initialize
const APP_VIEW = document.getElementById('app-view');
const NAV_BAR = document.getElementById('navbar');

window.addEventListener('beforeunload', unload);
window.addEventListener('load', init);

function unload(e) {
  // window.localStorage.setItem('unloadTime') = JSON.stringify(new Date());

};

function init() { 
	// if(window.localStorage.getItem('unloadTime')){
	// 	let loadTime = new Date();
	// 	let unloadTime = new Date(JSON.parse(window.localStorage.getItem('unloadTime')));
	// 	let refreshTime = loadTime.getTime() - unloadTime.getTime();

	// 	if(refreshTime>3000)//3000 milliseconds
	// 	{
	// 		window.localStorage.removeItem("authUser");
	// 		logout();
	// 	}
	// }
	loadView("login.view");
	$('.dropdown-toggle').dropdown();
}

//#endregion

// .____                     .___                   
// |    |    _________     __| _/___________  ______
// |    |   /  _ \__  \   / __ |/ __ \_  __ \/  ___/
// |    |__(  <_> ) __ \_/ /_/ \  ___/|  | \/\___ \ 
// |_______ \____(____  /\____ |\___  >__|  /____  >
//         \/         \/      \/    \/           \/ 
//#region loaders

async function loadView(pageToLoad) {
	console.log('in ' + pageToLoad);
	if (pageToLoad == 'home.view') {
		if (!localStorage.getItem('authUser')) {
			console.log('No user logged in, navigating to login screen');
			loadView("login.view");
			return;
		}
	}
	let xhr = new XMLHttpRequest;
	// xhr.responseType();
	xhr.open("GET", pageToLoad, true); // third parameter of this method is optional (defaults to true)
	xhr.send();
	xhr.onreadystatechange = async function () {
	if (xhr.readyState == 4 && xhr.status == 200) {
		APP_VIEW.innerHTML = xhr.responseText;
		switch (pageToLoad) {
		default:
			loadView('login.view');
			return;
		case 'login.view':
			configureLoginView();
			break;
		case 'home.view':
			configureHomeView();
			break;
		case 'profile.view':
			configureProfileView();
			break;
		case 'user_register.view':
			if (!isAuthorized(['admin'])) return;
			configureRegisterView();
			break;
		case 'user_update.view':
			if (!isAuthorized(['admin'])) return;
			configureUserUpdateView();
			break;
		case 'user_view.view':
		case 'user_delete.view':
			if (!isAuthorized(['admin'])) return;
			configureUsersView();
			break;
		case 'reimbursement_create.view':
			configureReimbursementCreateView();
			break;
		case 'reimbursement_update.view':
			configureReimbursementUpdateView();
			break;
		case 'reimbursement_view.view':
			configureReimbursementsView();
			break;
		}
		loadNavBar();
	}
	}
}

function eventLoadView(evt) {
  loadView(evt.currentTarget.desiredView);
}

function loadNavBar() {
  console.log('in loadNavBar');
  let xhr = new XMLHttpRequest;
  let destNav = "main.nav";
  let authUser = JSON.parse(localStorage.getItem('authUser'));
  var authRole = "main";
  if (authUser) {
	console.log(authUser);
	authRole = authUser.role.toLowerCase();
	destNav = authRole + ".nav";
  }
  console.log(destNav);
  xhr.open("GET", destNav, true);
  xhr.send();
  xhr.onreadystatechange = async function () {
	if (xhr.readyState == 4 && xhr.status == 200) {
	  NAV_BAR.innerHTML = xhr.responseText;
	  switch (authRole) {
		default:
		case 'main':
		  // configureNavbarUserDropdown();
		  document.getElementById('toLogin').addEventListener('click', login);
		  break;
		case 'admin':
		  configureNavbarUserDropdown();
		  configureNavbarReimbursementDropdown();
		  configureDefaultViewButtons();
		  document.getElementById('toLogout').addEventListener('click', logout);
		  break;
		case 'employee':
		  configureNavbarReimbursementDropdown();
		  configureDefaultViewButtons();
		  document.getElementById('toLogout').addEventListener('click', logout);
		  break;
		case 'manager':
		  configureNavbarReimbursementDropdown();
		  configureDefaultViewButtons();
		  document.getElementById('toLogout').addEventListener('click', logout);
		  break;
	  }
	}
  }
}

//#endregion

// _________                _____.__                            __  .__                      
// \_   ___ \  ____   _____/ ____\__| ____  __ ______________ _/  |_|__| ____   ____   ______
// /    \  \/ /  _ \ /    \   __\|  |/ ___\|  |  \_  __ \__  \\   __\  |/  _ \ /    \ /  ___/
// \     \___(  <_> )   |  \  |  |  / /_/  >  |  /|  | \// __ \|  | |  (  <_> )   |  \\___ \ 
//  \______  /\____/|___|  /__|  |__\___  /|____/ |__|  (____  /__| |__|\____/|___|  /____  >
//         \/            \/        /_____/                   \/                    \/     \/ 
//#region configurations

function configureLoginView() {
  console.log('in configureLoginView');

  document.getElementById('login-message').setAttribute('hidden', true);
  document.getElementById('login-button-container').addEventListener('mouseover', validateLoginForm);
  document.getElementById('login').addEventListener('click', login);
}

function configureRegisterView() {
  console.log('in configureRegisterView()');

  document.getElementById('reg-message').setAttribute('hidden', true);

  document.getElementById('reg-username').addEventListener('blur', isUsernameAvailable);
  document.getElementById('email').addEventListener('blur', isEmailAvailable);

  document.getElementById('register').setAttribute('disabled', true);
  document.getElementById('reg-button-container').addEventListener('mouseover', validateRegisterForm);
  document.getElementById('register').addEventListener('click', registerUserRequest);
}

function configureUserUpdateView(){
	console.log('in configureUserUpdateView()');
  
	document.getElementById('reg-message').setAttribute('hidden', true);
  
	document.getElementById('reg-username').addEventListener('blur', isUsernameAvailable);
	document.getElementById('email').addEventListener('blur', isEmailAvailable);
  
	document.getElementById('register').setAttribute('disabled', true);
	document.getElementById('reg-button-container').addEventListener('mouseover', validateUpdateForm);
	document.getElementById('register').addEventListener('click', updateUserRequest);
	populateUserUpdateInputFields();
}

function configureHomeView() {
  console.log('in configureHomeView');

  let authUser = JSON.parse(localStorage.getItem('authUser'));
  document.getElementById('loggedInUsername').innerText = authUser.username;
}

function configureUsersView() {
  console.log('in configureUsersView()');
  populateUserView();
}

function configureReimbursementUpdateView(){
	console.log('in configureReimbursementUpdateView();');

	document.getElementById('reg-message').setAttribute('hidden', true);

	document.getElementById('update-reimbursement').setAttribute('disabled', true);
	document.getElementById('sub-button-container').addEventListener('mouseover', validateReimbursementUpdateForm); 
	document.getElementById('update-reimbursement').addEventListener('click', updateReimbursementRequest); 
	// $('.file-upload').file_upload();
	populateReimbursementsUpdateView();

}
function configureReimbursementCreateView(){
	console.log('in configureReimbursementCreateView();');

	document.getElementById('reg-message').setAttribute('hidden', true);

	document.getElementById('submit-reimbursement').setAttribute('disabled', true);
	document.getElementById('sub-button-container').addEventListener('mouseover', validateReimbursementCreateForm);
	document.getElementById('submit-reimbursement').addEventListener('click', submitReimbursementRequest);
	// $('.file-upload').file_upload();

}

function configureReimbursementsView(){
	console.log('in configureReimbursementsView()');
	populateReimbursementsView();

}

function configureDefaultViewButtons() {
  console.log('in configureDefaultViewButtons');
  const HomeButton = document.getElementById('toHome')
  HomeButton.addEventListener('click', eventLoadView);
  HomeButton.desiredView = 'home.view';

  const ProfileButton = document.getElementById('toProfile')
  ProfileButton.addEventListener('click', eventLoadView);
  ProfileButton.desiredView = 'profile.view';
}

function configureNavbarUserDropdown() {
  // const UserDropdown = document.getElementById('dropdownMenuButtonUser');
  // UserDropdown.addEventListener('click', function(){
  // 	$('.dropdown-toggle').dropdown('toggle');
  // });

  const UpdateUserButton = document.getElementById('toUpdateUser');
  UpdateUserButton.addEventListener('click', eventLoadView);
  UpdateUserButton.desiredView = 'user_update.view';

  const RegisterUserButton = document.getElementById('toRegisterUser');
  RegisterUserButton.addEventListener('click', eventLoadView);
  RegisterUserButton.desiredView = 'user_register.view';

  const ViewUserButton = document.getElementById('toViewUsers');
  ViewUserButton.addEventListener('click', eventLoadView);
  ViewUserButton.desiredView = 'user_view.view';

  const DeleteUserButton = document.getElementById('toDeleteUser');
  DeleteUserButton.addEventListener('click', eventLoadView);
  DeleteUserButton.desiredView = 'user_delete.view';
}

function configureNavbarReimbursementDropdown() {
  // const ReimbursementDropdown = document.getElementById('dropdownMenuButtonReimbursement');
  // ReimbursementDropdown.addEventListener('click', function(){
  // 	$('.dropdown-toggle').dropdown('toggle');
  // });

  const CreateReimbursementButton = document.getElementById('toCreateReimbursement');
  CreateReimbursementButton.addEventListener('click', eventLoadView);
  CreateReimbursementButton.desiredView = 'reimbursement_create.view';

  const UpdateReimbursementButton = document.getElementById('toUpdateReimbursement');
  UpdateReimbursementButton.addEventListener('click', eventLoadView);
  UpdateReimbursementButton.desiredView = 'reimbursement_update.view';

  const ViewReimbursementButton = document.getElementById('toViewReimbursement');
  ViewReimbursementButton.addEventListener('click', eventLoadView);
  ViewReimbursementButton.desiredView = 'reimbursement_view.view';

  const DeleteReimbursementButton = document.getElementById('toDeleteReimbursement');
  DeleteReimbursementButton.addEventListener('click', eventLoadView);
  DeleteReimbursementButton.desiredView = 'reimbursement_view.view';
}

//#endregion

// ________                              __  .__                      
// \_____  \ ______   ________________ _/  |_|__| ____   ____   ______
//  /   |   \\____ \_/ __ \_  __ \__  \\   __\  |/  _ \ /    \ /  ___/
// /    |    \  |_> >  ___/|  | \// __ \|  | |  (  <_> )   |  \\___ \ 
// \_______  /   __/ \___  >__|  (____  /__| |__|\____/|___|  /____  >
//         \/|__|        \/           \/                    \/     \/ 
//#region operations

function login() {
  console.log('in login()');

  let un = document.getElementById('login-username').value;
  let pw = document.getElementById('login-password').value;

  let credentials = {
	username: un,
	password: pw
  }

  let credentialsJSON = JSON.stringify(credentials);

  let xhr = new XMLHttpRequest;
  xhr.open('POST', 'auth');
  xhr.setRequestHeader('Content-type', 'application/json');
  xhr.send(credentialsJSON);

  xhr.onreadystatechange = function () {
	if (xhr.readyState == 4 && xhr.status == 200) {

	  console.log('login successful!');
	  document.getElementById('login-message').setAttribute('hidden', true);
	  localStorage.setItem('authUser', xhr.responseText);
	  loadView('home.view');

	} else if (xhr.readyState == 4 && xhr.status == 401) {

	  document.getElementById('login-message').removeAttribute('hidden');
	  let err = JSON.parse(xhr.responseText);
	  document.getElementById('login-message').innerText = err.message;

	}
  }
}

function logout() {
	console.log('in logout()');
	localStorage.removeItem('authUser');
	loadView("login.view");
	let xhr = new XMLHttpRequest();
	xhr.open('GET', 'auth');
	xhr.send();
	xhr.onreadystatechange = function () {
		if (xhr.readyState == 4 && xhr.status == 204) {
			console.log('logout successful!');
		}
	}
}

function registerUserRequest() {
	console.log('in register()');

	let fn = document.getElementById('fn').value;
	let ln = document.getElementById('ln').value;
	let em = document.getElementById('email').value;
	let un = document.getElementById('reg-username').value;
	let pw = document.getElementById('reg-password').value;
	let rl = document.getElementById('roleSelectButton').value;

	let credentials = {
		email: em,
		firstName: fn,
		lastName: ln,
		username: un,
		password: pw,
		role: rl
	}
	console.log(credentials);

	let credentialsJSON = JSON.stringify(credentials);

	let xhr = new XMLHttpRequest;
	xhr.open('POST', 'users');
	xhr.setRequestHeader('Content-type', 'application/json');
	xhr.send(credentialsJSON);

	xhr.onreadystatechange = function () {
		if (xhr.readyState == 4 && xhr.status == 201) {

			let msg = JSON.parse(xhr.responseText);
			console.log(msg);
			// document.getElementById('reg-message').innerText = msg;
			document.getElementById('reg-message').setAttribute('hidden', true);
			loadView("user_register.view");

		} else if (xhr.readyState == 4 && xhr.status == 400) {

			document.getElementById('reg-message').removeAttribute('hidden');
			let err = JSON.parse(xhr.responseText);
			document.getElementById('reg-message').innerText = err.message;

		}
	}
}

function submitReimbursementRequest(){
	console.log('in submitReimbursementRequest()');

	let amt = document.getElementById('amount').value;
	let desc = document.getElementById('description').value;
	let rcpt =  ''; //document.getElementById('').value; //TODO coming soon
	let author = JSON.parse(localStorage.getItem('authUser')).username;
	let reimb_type = document.getElementById('reimbursementType').value;
	
	let reimbursementDto = {
		amount:amt,
		description:desc,
		receiptURI:rcpt,
		author:author,
		type:reimb_type
	}
	console.log(reimbursementDto);
	
	let reimbDtoJSON = JSON.stringify(reimbursementDto);

	let xhr = new XMLHttpRequest;
	xhr.open('POST', 'reimbursements');
	xhr.setRequestHeader('Content-type', 'application/json');
	xhr.send(reimbDtoJSON);

	xhr.onreadystatechange = function () {
		if (xhr.readyState == 4 && xhr.status == 201) {

			let msg = JSON.parse(xhr.responseText);
			console.log(msg);
			// document.getElementById('reg-message').innerText = msg;
			document.getElementById('reg-message').setAttribute('hidden', true);
			loadView("reimbursement_create.view");

		} else if (xhr.readyState == 4 && xhr.status == 400) {

			document.getElementById('reg-message').removeAttribute('hidden');
			let err = JSON.parse(xhr.responseText);
			document.getElementById('reg-message').innerText = err.message;

		}
	}
}

function updateUserRequest() {
	console.log('in update()');

	let eid = document.getElementById('id').value;
	let ac = document.getElementById('active').checked == true;
	let fn = document.getElementById('fn').value;
	let ln = document.getElementById('ln').value;
	let em = document.getElementById('email').value;
	let un = document.getElementById('reg-username').value;
	let pw = document.getElementById('reg-password').value;
	let rl = document.getElementById('roleSelectButton').value;

	let userDTO = {
		id: eid,
		active: ac,
		email: em,
		firstName: fn,
		lastName: ln,
		username: un,
		password: pw,
		role: rl
	}
	console.log(userDTO);

	let userDTOJSON = JSON.stringify(userDTO);

	let xhr = new XMLHttpRequest;
	xhr.open('PUT', 'users');
	xhr.setRequestHeader('Content-type', 'application/json');
	xhr.send(userDTOJSON);

	xhr.onreadystatechange = function () {
		if (xhr.readyState == 4 && xhr.status == 200) {

		let msg = JSON.parse(xhr.responseText);
		console.log(msg);
		// document.getElementById('reg-message').innerText = msg;
		document.getElementById('reg-message').setAttribute('hidden', true);
		loadView("user_view.view");

		} else if (xhr.readyState == 4 && xhr.status == 400) {

		document.getElementById('reg-message').removeAttribute('hidden');
		let err = JSON.parse(xhr.responseText);
		document.getElementById('reg-message').innerText = err.message;

		}
	}
}

function updateReimbursementRequest(){
	console.log('in updateReimbursementRequest()');

	let rid = document.getElementById('id').value;
	let amt = document.getElementById('amount').checked == true;
	let sub = document.getElementById('submitted').value;
	let res = document.getElementById('resolved').value;
	let des = document.getElementById('description').value;
	let rec = document.getElementById('receipts').value;
	let aut = document.getElementById('author').value;
	let olv = document.getElementById('resolver').value;
	let sta = document.getElementById('reimbursementStatus').value;
	let typ = document.getElementById('reimbursementType').value;

	let reimbursementDTO = {
		id:rid,
		amount:amt,
		submitted:sub,
		resolved:res,
		description:des,
		receiptURI:rec,
		author:aut,
		resolver:olv,
		reimb_status_id:sta,
		reimb_type_id:typ
	}
	console.log(reimbursementDTO);

	let reimbursementDTOJSON = JSON.stringify(reimbursementDTO);

	let xhr = new XMLHttpRequest;
	xhr.open('PUT', 'reimbursements');
	xhr.setRequestHeader('Content-type', 'application/json');
	xhr.send(reimbursementDTOJSON);

	xhr.onreadystatechange = function () {
		if (xhr.readyState == 4 && xhr.status == 200) {

		let msg = JSON.parse(xhr.responseText);
		console.log(msg);
		// document.getElementById('reg-message').innerText = msg;
		document.getElementById('reg-message').setAttribute('hidden', true);
		loadView("reimbursement_view.view");

		} else if (xhr.readyState == 4 && xhr.status == 400) {

		document.getElementById('reg-message').removeAttribute('hidden');
		let err = JSON.parse(xhr.responseText);
		document.getElementById('reg-message').innerText = err.message;

		}
	}

}

function populateUserView() {
  console.log('in populateUserView');

  let tableBody = document.getElementById('userTableBody');

  let xhr = new XMLHttpRequest();

  xhr.open('GET', 'users');
  xhr.setRequestHeader('Content-type', 'application/json');
  xhr.send();

  xhr.onreadystatechange = function () {
	if (xhr.readyState == 4 && xhr.status == 200) {
		for(let child of tableBody.childNodes){
			child.remove();
		}
		let users = JSON.parse(xhr.responseText);
		for (var i = 0; i < users.length; i++) {
			let row = document.createElement("tr");
			row.classList.add("userEntry");
			let active = users[i].active;
			if (!active) {
				row.classList.add('disabled');
			}
			let id = document.createElement("td");
			id.innerText = users[i].id;
			row.id = users[i].id;
			let firstName = document.createElement("td");
			firstName.innerText = users[i].firstName;
			let lastName = document.createElement("td");
			lastName.innerText = users[i].lastName;
			let username = document.createElement("td");
			username.innerText = users[i].username;
			let email = document.createElement("td");
			email.innerText = users[i].email;
			let role = document.createElement("td");
			role.innerText = titleCase(users[i].role);
			let modify = document.createElement('td');
			modify.id = `modify:${users[i].id}`;

			let pid = users[i].id;
			let ac = users[i].active;
			let em = users[i].email;
			let un = users[i].username;
			let rl = titleCase(users[i].role);

			let principal = {
				"id": pid,
				"username": un,
				"role": rl
			}

			let user = {
				id: pid,
				active: ac,
				username: un,
				email: em,
				role: rl
			}

			let editInteract = document.createElement('a');
			editInteract.href = "#";
			editInteract.className += 'edit';
			editInteract.innerText = "Edit";
			editInteract.addEventListener('click', eventUpdateUser);
			editInteract.user = user;
			modify.appendChild(editInteract);
			let slash = document.createTextNode(' / ');
			modify.appendChild(slash);
			if (active) {
				let deactivateInteract = document.createElement('a');
				deactivateInteract.id = `deactivate:${users[i].id}`;
				deactivateInteract.href = "#";
				deactivateInteract.className += 'remove';
				deactivateInteract.innerText = "Deactivate";
				deactivateInteract.addEventListener('click', eventDeactivateUser);
				deactivateInteract.principal = principal;
				modify.appendChild(deactivateInteract);
			} else {
				let reactivateInteract = document.createElement('a');
				reactivateInteract.id = `reactivate:${users[i].id}`;
				reactivateInteract.href = "#";
				reactivateInteract.className += 'remove';
				reactivateInteract.innerText = "Activate";
				reactivateInteract.addEventListener('click', eventActivateUser);
				reactivateInteract.principal = principal;
				modify.appendChild(reactivateInteract);
			}

			row.appendChild(id);
			row.appendChild(firstName);
			row.appendChild(lastName);
			row.appendChild(username);
			row.appendChild(email);
			row.appendChild(role);
			row.appendChild(modify);

			tableBody.appendChild(row);
			}
			$('#userTable').DataTable();
			$('.dataTables_length').addClass('bs-select');
			console.log(users);
			// tableBody.innerText = JSON.stringify(users);
		}
	}
}

function populateReimbursementsView(){
	
	console.log('in populateReimbursementsView');

	let tableBody = document.getElementById('reimbursementTableBody');

	let xhr = new XMLHttpRequest();
	let authUser = JSON.parse(localStorage.getItem('authUser'));
	if(isAuthorized(['admin','manager'])){
		xhr.open('GET', 'reimbursements');
	} else {
		xhr.open('GET', `reimbursements?author=${authUser.id}`);
	}
	xhr.setRequestHeader('Content-type', 'application/json');
	xhr.send();

	xhr.onreadystatechange = function () {
	if (xhr.readyState == 4 && xhr.status == 200) {
		//remove previous table states, if any.
		for(let child of tableBody.childNodes){
			child.remove();
		}
		let reimbursements = JSON.parse(xhr.responseText);
		for (var i = 0; i < reimbursements.length; i++) {
			let row = document.createElement("tr");
			row.classList.add("reimbursementEntry");

			let rid = reimbursements[i].id;
			let amt = reimbursements[i].amount;
			let submtd = reimbursements[i].submitted;
			if(submtd) submtd = (new Date(submtd)).toString();
			let rslvd = reimbursements[i].resolved;
			if(rslvd) rslvd = (new Date(rslvd)).toString();
			let desc = reimbursements[i].description;
			let rcptURI = reimbursements[i].receiptURI;
			let authr = reimbursements[i].author;
			if(authr) authr = authr.username;
			let rslvr = reimbursements[i].resolver;
			if(rslvr) rslvr = rslvr.username;
			let reimb_status = reimbursements[i].reimb_status_id;
			if(reimb_status) reimb_status = titleCase(reimb_status);
			let reimb_type= reimbursements[i].reimb_type_id;
			if(reimb_type) reimb_type = titleCase(reimb_type);

			let idElem = document.createElement("td");
			idElem.innerText = rid;
			row.id = rid;
			let amountElem = document.createElement("td");
			amountElem.innerText = amt;
			let submittedElem = document.createElement("td");
			submittedElem.innerText = submtd;
			let resolvedElem = document.createElement("td");
			resolvedElem.innerText = rslvd;
			let descriptionElem = document.createElement("td");
			descriptionElem.innerText = desc;
			let receiptURIElem = document.createElement("td");
			receiptURIElem.innerText = rcptURI;
			let authorElem = document.createElement("td");
			authorElem.innerText = authr;
			let resolverElem = document.createElement("td");
			resolverElem.innerText = rslvr;
			let reimbursementStatusElem = document.createElement("td");
			reimbursementStatusElem.innerText = reimb_status;
			let reimbursementTypeElem = document.createElement("td");
			reimbursementTypeElem.innerText = reimb_type;
			let modifyElem = document.createElement('td');
			modifyElem.id = `modify:${rid}`;

			let reimbursement = {
				id:rid,
				amount:amt,
				submitted:submtd,
				resolved:rslvd,
				description:desc,
				receiptURI:rcptURI,
				author:authr,
				resolver:rslvr,
				reimbursementStatus:reimb_status,
				reimbursementType:reimb_type
			}

			let deleteId = {
				id:rid
			}

			let editInteract = document.createElement('a');
			editInteract.href = "#";
			editInteract.className += 'edit';
			editInteract.innerText = "Edit";
			editInteract.addEventListener('click', eventUpdateReimbursement);
			editInteract.reimbursement = reimbursement;
			modifyElem.appendChild(editInteract);

			let slash = document.createTextNode(' / ');
			modifyElem.appendChild(slash);

			let deactivateInteract = document.createElement('a');
			deactivateInteract.id = `deactivate:${reimbursements[i].id}`;
			deactivateInteract.href = "#";
			deactivateInteract.className += 'remove';
			deactivateInteract.innerText = "Delete";
			deactivateInteract.addEventListener('click', eventDeleteReimbursement);
			deactivateInteract.id = deleteId;
			modifyElem.appendChild(deactivateInteract);
			

			row.appendChild(idElem);
			row.appendChild(amountElem);
			row.appendChild(submittedElem);
			row.appendChild(resolvedElem);
			row.appendChild(descriptionElem);
			row.appendChild(receiptURIElem);
			row.appendChild(authorElem);
			row.appendChild(resolverElem);
			row.appendChild(reimbursementStatusElem);
			row.appendChild(reimbursementTypeElem);
			row.appendChild(modifyElem);

			tableBody.appendChild(row);
			}
		$('#reimbursementTable').DataTable();
		$('.dataTables_length').addClass('bs-select');
		console.log(reimbursements);
		// tableBody.innerText = JSON.stringify(reimbursements);
		}
	}
}

function populateUserUpdateInputFields(){
	console.log('in populateUserUpdateInputFields()');
	
	let user = JSON.parse(localStorage.getItem('updateUser'));
	console.log(user);
	
	let updateViewHeader = document.getElementById('updateViewHeader');
	updateViewHeader.innerText = 'Revabursement Update: Updating ' + user.username;
	let idBox = document.getElementById('id');
	idBox.removeAttribute('disabled');
	idBox.innerText = user.id;
	idBox.value = user.id;
	idBox.disabled = true;
	let roleSelect = document.getElementById('roleSelectButton');
	for (let i = 0; i < roleSelect.options.length; i++) {
		const opt = roleSelect.options[i];
		console.log(user);
		if(opt.value == user.role.toLowerCase()){
			opt.selected = true;
			break;
		}
	}
	localStorage.removeItem('updateUser');
}

function populateReimbursementsUpdateView(){
	console.log('in populateReimbursementsUpdateView()');
	
	let reimbursement = JSON.parse(localStorage.getItem('updateReimbursement'));
	console.log(reimbursement);

	// let reimbursement = {
		// id:rid,
	// 	amount:amt,
		// submitted:submtd,
	// 	resolved:rslvd,
	// 	description:desc,
	// 	receiptURI:rcptURI,
		// author:authr,
		// resolver:rslvr,
		// 	reimbursementStatus:reimb_status,
		// 	reimbursementType:reimb_type
	// }
	
	let updateViewHeader = document.getElementById('updateViewHeader');
	updateViewHeader.innerText = 'Revabursement Update: Updating Reimbursement for ' + reimbursement.author;

	editDisabledValue('id', reimbursement.id);
	editDisabledValue('submitted', reimbursement.submitted);
	editDisabledValue('author', reimbursement.author);
	editDisabledValue('resolver', reimbursement.resolver);
	selectChosenOption('reimbursementStatus', reimbursement.reimbursementStatus);
	selectChosenOption('reimbursementType', reimbursement.reimbursementType);

	let amount = document.getElementById('amount');
	amount.value = reimbursement.amount;
	let resolved = document.getElementById('resolved');
	resolved.value = reimbursement.resolved;
	let description = document.getElementById('description');
	description.value = reimbursement.description;
	let receiptURI = document.getElementById('receiptURI');
	receiptURI.value = reimbursement.receiptURI;
	
	localStorage.removeItem('updateReimbursement');
}

function selectChosenOption(id, option){
	let selection = document.getElementById(id);
	for (let i = 0; i < selection.options.length; i++) {
		const opt = selection.options[i];
		if(opt.value == option.toLowerCase()){
			opt.selected = true;
			break;
		}
	}

}

// Edit record
function UpdateUser(user) {
	localStorage.setItem('updateUser', JSON.stringify(user));
	loadView('user_update.view');
}

function eventUpdateUser(evt) {
  UpdateUser(evt.currentTarget.user);
}
// Delete a record
function deleteUser(principal) {
  console.log(principal);
  if (!principal) return;
  let xhr = new XMLHttpRequest();
  xhr.open('DELETE', 'users');
  xhr.setRequestHeader('Content-type', 'application/json');
  xhr.send(JSON.stringify(principal));

  xhr.onreadystatechange = function () {
	if (xhr.readyState == 4 && xhr.status == 204) {
	  console.log('User removed successfully!');
	  let deleteMe = document.getElementById(principal.id);
	  deleteMe.remove();
	}
  }
}
function eventDeleteUser(evt) {
  deleteUser(evt.currentTarget.principal);
}
/**
 * 
 * @param {Principal} principal 
 */
function deactivateUser(principal) {
  console.log(principal);
  if (!principal) return;
  if(isSelf(principal)){
	  console.log(`You cannot deactivate yourself.`);
	  return;
  }
  let xhr = new XMLHttpRequest();
  xhr.open('DELETE', 'users');
  xhr.setRequestHeader('Content-type', 'application/json');
  xhr.send(JSON.stringify(principal));

  xhr.onreadystatechange = function () {
	if (xhr.readyState == 4 && xhr.status == 204) {
	  console.log('User removed successfully!');
	  let disabledRow = document.getElementById(principal.id);
	  disabledRow.classList.add('disabled');
	  let deactivateInteract = document.getElementById(`deactivate:${principal.id}`);
	  deactivateInteract.remove();
	  
	  let reactivateInteract = document.createElement('a');
	  reactivateInteract.id = `reactivate:${principal.id}`;
	  reactivateInteract.href = "#";
	  reactivateInteract.className += 'remove';
	  reactivateInteract.innerText = "Activate";
	  reactivateInteract.addEventListener('click', eventActivateUser);
	  reactivateInteract.principal = principal;

	  let modify = document.getElementById(`modify:${principal.id}`);
	  modify.appendChild(reactivateInteract);

	}
  }
}
function eventDeactivateUser(evt) {
  deactivateUser(evt.currentTarget.principal);
}
/**
 * 
 * @param {Principal} principal 
 */
function activateUser(principal) {
  console.log(principal);
  if (!principal) return;
  let xhr = new XMLHttpRequest();
  xhr.open('PUT', 'users');
  xhr.setRequestHeader('Content-type', 'application/json');
  xhr.setRequestHeader('activate', true);
  xhr.send(JSON.stringify(principal));

  xhr.onreadystatechange = function () {
	if (xhr.readyState == 4 && xhr.status == 204) {
	  console.log('User reactivated successfully!');
	  let deleteMe = document.getElementById(principal.id);
	  deleteMe.classList.remove('disabled');
	  let reactivateInteract = document.getElementById(`reactivate:${principal.id}`);
	  reactivateInteract.remove();

	  let deactivateInteract = document.createElement('a');
	  deactivateInteract.id = `deactivate:${principal.id}`;
	  deactivateInteract.href = "#";
	  deactivateInteract.className += 'remove';
	  deactivateInteract.innerText = "Deactivate";
	  deactivateInteract.addEventListener('click', eventDeactivateUser);
	  deactivateInteract.principal = principal;

	  let modify = document.getElementById(`modify:${principal.id}`);
	  modify.appendChild(deactivateInteract);

	}
	
  }
}
function eventActivateUser(evt) {
  activateUser(evt.currentTarget.principal);
}

function updateReimbursement(reimbursement){
	localStorage.setItem('updateReimbursement', JSON.stringify(reimbursement));
	loadView('reimbursement_update.view');
}

function eventUpdateReimbursement(evt){
	console.log('in eventUpdateReimbursement()');
	updateReimbursement(evt.currentTarget.reimbursement);
	
}

function deleteReimbursement(id){
	id = JSON.stringify(id);
	console.log(id);
	if (!id) return;
	let xhr = new XMLHttpRequest();
	xhr.open('DELETE', 'reimbursements');
	xhr.setRequestHeader('Content-type', 'application/json');
	xhr.send(id);
  
	xhr.onreadystatechange = function () {
	  if (xhr.readyState == 4 && xhr.status == 204) {
		console.log('User removed successfully!');
		let deleteMe = document.getElementById(id.id);
		deleteMe.remove();
	  }
	}
}

function eventDeleteReimbursement(evt){
	console.log('in eventDeleteReimbursement()');
	deleteReimbursement(evt.currentTarget.id);
}

//#endregion

// ___ ___         .__                        ___________                   __  .__                      
// /   |   \   ____ |  | ______   ___________  \_   _____/_ __  ____   _____/  |_|__| ____   ____   ______
// /    ~    \_/ __ \|  | \____ \_/ __ \_  __ \  |    __)|  |  \/    \_/ ___\   __\  |/  _ \ /    \ /  ___/
// \    Y    /\  ___/|  |_|  |_> >  ___/|  | \/  |     \ |  |  /   |  \  \___|  | |  (  <_> )   |  \\___ \ 
// \___|_  /  \___  >____/   __/ \___  >__|     \___  / |____/|___|  /\___  >__| |__|\____/|___|  /____  >
// 	  \/       \/     |__|        \/             \/             \/     \/                    \/     \/ 
//#region helper functions
function titleCase(string) {
  var sentence = string.toLowerCase().split(" ");
  for (var i = 0; i < sentence.length; i++) {
	sentence[i] = sentence[i][0].toUpperCase() + sentence[i].slice(1);
  }
  return (sentence.join(" "));
}

function isAuthorized(listOfRoles){
	let authUser = JSON.parse(localStorage.getItem('authUser'));
	if (!authUser) {
		return false;
	}
	let authorized = false;
	for (var i = 0; i < listOfRoles.length; i++) {
		if (authUser.role.toLowerCase() == listOfRoles[i]){
			authorized = true;
		}
	}
	return authorized;
}

function editDisabledValue(id, newVal){
	let element = document.getElementById(id);
	element.removeAttribute('disabled');
	element.value = newVal;
	element.disabled = true;
}
//#endregion

// ___________                    ____   ____      .__  .__    .___       __  .__               
// \_   _____/__________  _____   \   \ /   /____  |  | |__| __| _/____ _/  |_|__| ____   ____  
//  |    __)/  _ \_  __ \/     \   \   Y   /\__  \ |  | |  |/ __ |\__  \\   __\  |/  _ \ /    \ 
//  |     \(  <_> )  | \/  Y Y  \   \     /  / __ \|  |_|  / /_/ | / __ \|  | |  (  <_> )   |  \
//  \___  / \____/|__|  |__|_|  /    \___/  (____  /____/__\____ |(____  /__| |__|\____/|___|  /
//      \/                    \/                 \/             \/     \/                    \/ 
//#region formValidation

function isUsernameAvailable() {

	console.log('in isUsernameAvailable()');

	let username = document.getElementById('reg-username').value;

	let xhr = new XMLHttpRequest();

	xhr.open('POST', 'username.validate');
	xhr.setRequestHeader('Content-type', 'application/json');
	xhr.send(JSON.stringify(username));

	xhr.onreadystatechange = function () {
	if (xhr.readyState == 4 && xhr.status == 204) {
		console.log('Provided username is available!');
		document.getElementById('reg-message').setAttribute('hidden', true);
	} else if (xhr.readyState == 4 && xhr.status == 409) {
		document.getElementById('reg-message').removeAttribute('hidden')
		document.getElementById('reg-message').innerText = 'The provided username is already taken!';
		document.getElementById('register').setAttribute('disabled', true);
	}
	}
}

function isEmailAvailable() {

	console.log('in isEmailAvailable()');

	let email = document.getElementById('email').value;

	let xhr = new XMLHttpRequest();

	xhr.open('POST', 'email.validate');
	xhr.setRequestHeader('Content-type', 'application/json');
	xhr.send(JSON.stringify(email));

	xhr.onreadystatechange = function () {
	if (xhr.readyState == 4 && xhr.status == 204) {
		console.log('Provided email is available!');
		document.getElementById('reg-message').setAttribute('hidden', true);
	} else if (xhr.readyState == 4 && xhr.status == 409) {
		document.getElementById('reg-message').removeAttribute('hidden');
		document.getElementById('reg-message').innerText = 'The provided email address is already taken!';
		document.getElementById('register').setAttribute('disabled', true);
	}
	}
}

function validateLoginForm() {

	console.log('in validateLoginForm()');

	let msg = document.getElementById('login-message').innerText;

	if (msg == 'User authentication failed!') {
		return;
	}

	let un = document.getElementById('login-username').value;
	let pw = document.getElementById('login-password').value;

	if (!un || !pw) {
		document.getElementById('login-message').removeAttribute('hidden');
		document.getElementById('login-message').innerText = 'You must provided values for all fields in the form!'
		document.getElementById('login').setAttribute('disabled', true);
	} else {
		document.getElementById('login').removeAttribute('disabled');
		document.getElementById('login-message').setAttribute('hidden', true);
	}

}

function validateRegisterForm() {

	console.log('in validateRegisterForm()');

	let fn = document.getElementById('fn').value;
	let ln = document.getElementById('ln').value;
	let email = document.getElementById('email').value;
	let un = document.getElementById('reg-username').value;
	let pw = document.getElementById('reg-password').value;
	let rl = document.getElementById('roleSelectButton').value;

	if (!fn || !ln || !email || !un || !pw || !rl) {
		document.getElementById('reg-message').removeAttribute('hidden');
		document.getElementById('reg-message').innerText = 'You must provide values for all fields in the form!'
		document.getElementById('register').setAttribute('disabled', true);
	} else {
		document.getElementById('register').removeAttribute('disabled');
		document.getElementById('reg-message').setAttribute('hidden', true);
	}
}

function validateUpdateForm() {

	console.log('in validateRegisterForm()');

	let fn = document.getElementById('fn').value;
	let ln = document.getElementById('ln').value;
	let email = document.getElementById('email').value;
	let un = document.getElementById('reg-username').value;
	let pw = document.getElementById('reg-password').value;
	let rl = document.getElementById('roleSelectButton').value;

	if (fn || ln || email || un || pw || rl) {
		document.getElementById('register').removeAttribute('disabled');
		document.getElementById('reg-message').setAttribute('hidden', true);
	} else {
		document.getElementById('reg-message').removeAttribute('hidden');
		document.getElementById('reg-message').innerText = 'You have not changed a single value in the form!'
		document.getElementById('register').setAttribute('disabled', true);
	}
}

function validateReimbursementCreateForm(){
	console.log('in validateReimbursementForm()');
	
	let amnt = document.getElementById('amount').value;
	let desc = document.getElementById('description').value;
	let recpt = document.getElementById('receipts').value;
	
	if (amnt && desc) {
		document.getElementById('submit-reimbursement').removeAttribute('disabled');
		document.getElementById('reg-message').setAttribute('hidden', true);
	} else {
		document.getElementById('reg-message').removeAttribute('hidden');
		document.getElementById('reg-message').innerText = 'You need an amount and a description!';
		document.getElementById('submit-reimbursement').setAttribute('disabled', true);
	}
}

function validateReimbursementUpdateForm(){
	console.log('in validateReimbursementUpdateForm()');
	
	let amnt = document.getElementById('amount').value;
	let desc = document.getElementById('description').value;
	let recpt = document.getElementById('receipts').value;
	
	if (amnt && desc) {
		document.getElementById('update-reimbursement').removeAttribute('disabled');
		document.getElementById('reg-message').setAttribute('hidden', true);
	} else {
		document.getElementById('reg-message').removeAttribute('hidden');
		document.getElementById('reg-message').innerText = 'You need an amount and a description!';
		document.getElementById('update-reimbursement').setAttribute('disabled', true);
	}
}

function isSelf(principal){
	let authUser = JSON.parse(localStorage.getItem('authUser'));
	return principal.id == authUser.id;
}
//#endregion