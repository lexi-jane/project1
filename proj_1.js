// ok, first let's imagine we have a simple list with student names. 
const studNames = ['Jared', 'Amy', 'Kyle', 'James', "Anderson"]
//Create empty lists for each group
const groupOne = []
const groupTwo = []
const groupThree = []
const groupFour = []

//print list
console.log(studNames)

// copy the list, which we will edit until we don't have any other students
const studNotInGroups = studNames
//print list to make sure it's the same
console.log(studNotInGroups)

//function loop start here
// pick a random location from the list
const random = Math.floor(Math.random() * studNames.length);
console.log("Location in list:", random, "Name: ", studNames[random]);
// assign name to group
//edit list  

