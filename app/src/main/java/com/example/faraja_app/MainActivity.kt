package com.example.faraja_app

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable

import androidx.compose.runtime.rememberCoroutineScope

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.faraja_app.ui.theme.Faraja_AppTheme
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.navigation.navArgument
import androidx.navigation.NavType
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.filled.ArrowBack



@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Faraja_AppTheme {

                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "mycommunities") {
                    composable("mycommunities") { MyCommunitiesScreen(navController) }
                    composable("counselors") {CounselorsScreen(navController) }
                    composable("newcommunities") { NewCommunitiesScreen(navController)}
                    composable("currentcommunity") { CurrentCommunityScreen(navController)}
                    composable("counselorchat") { CounselorChat(navController = navController)}
                    composable(
                        "counselorchat/{counselorId}",
                        arguments = listOf(navArgument("counselorId") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val counselorId = backStackEntry.arguments?.getString("counselorId")
                        if (counselorId != null) {
                            CounselorChat(navController)
                        } else {
                            // Handle the case when counselorId is null
                            // You may want to show an error message or navigate back
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationSection(
    navController: NavHostController,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    TopAppBar(
        title = { /* No title here */ },
        navigationIcon = {
            IconButton(onClick = { /* Handle drawer opening */ }) {
                Icon(Icons.Filled.Menu, contentDescription = "Menu")
            }
        },
        actions = {
            Row(Modifier.fillMaxWidth()) {
                Spacer(Modifier.weight(1f)) // Spacer before the buttons

                // Communities Button with fixed width
                Button(
                    onClick = { navController.navigate("mycommunities") },
                    modifier = Modifier.widthIn(min = 140.dp)
                ) {
                    Text("My Communities")
                }

                Spacer(Modifier.width(8.dp)) // Spacer between buttons

                // Counselors Button with fixed width
                Button(
                    onClick = { navController.navigate("counselors") },
                    modifier = Modifier.widthIn(min = 140.dp)
                ) {
                    Text("Counselors")
                }

                Spacer(Modifier.weight(1f)) // Spacer after the buttons
            }
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MyCommunitiesScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            NavigationSection(
                navController = navController,
                selectedOption = "My Communities",
                onOptionSelected = { /* Handle option selection */ }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("newcommunities") },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Add")
            }
        }
    ) {
        CommunitiesClickable(myCommunities = MyCommunityData.MycommunityList, navController = navController)
    }
}


@Composable
fun CommunitiesClickable(myCommunities: List<MyCommunity>, navController: NavHostController) {
    LazyColumn(modifier = Modifier.padding(top = 75.dp)) {
        items(myCommunities) { myCommunity ->
            CommunityCardClickable(myCommunity = myCommunity) {
                // Navigate to the CurrentCommunityScreen when a card is clicked
                navController.navigate("currentcommunity")
            }
        }
    }
}



@Composable
fun CommunityCardClickable(myCommunity: MyCommunity, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Image(
                painter = painterResource(R.drawable.guitar), // Replace with actual image resource or painter
                contentDescription = "Community profile picture",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = myCommunity.name, // lowercase 'm' in myCommunity
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${myCommunity.members} members", // lowercase 'm' in myCommunity
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = myCommunity.description, // lowercase 'm' in myCommunity
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrentCommunityScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            NavigationSection(
                navController = navController,
                selectedOption = "Current Community",
                onOptionSelected = { /* Handle option selection */ }
            )
        }
    ) {
        StoryList(StoriesData.storyList)
    }
}

@Composable
fun StoryList(stories: List<Stories>) {
    LazyColumn {
        item {
            Spacer(modifier = Modifier.height(60.dp))  // Adjust the height as needed
        }

        // Then list the stories
        items(stories) { story ->
            StoryCard(story)
        }
    }
}


@Composable
fun StoryCard(story: Stories) {
    Card(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = story.username, style = MaterialTheme.typography.titleMedium)
            Text(text = story.community, style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = story.description, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            StoryActions()
        }
    }
}

@Composable
fun StoryActions() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(Icons.Default.FavoriteBorder, contentDescription = "Like")
        Icon(Icons.Default.Add, contentDescription = "Repost")
        Icon(Icons.Default.Email, contentDescription = "Comment")
        Icon(Icons.Default.Share, contentDescription = "Share")
    }
}

@Composable
fun CounselorCard(counselor: Counselor, onClick: (String) -> Unit) {
    Column(
        modifier = Modifier
            .clickable(onClick = { onClick(counselor.id) })
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(counselor.profilePictureResId),
                contentDescription = "Counselor profile picture",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(text = counselor.name, style = MaterialTheme.typography.titleSmall)
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = counselor.lastMessage,
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = counselor.lastMessageTime,
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun Counselors(counselors: List<Counselor>, navController: NavHostController) {
    LazyColumn {
        items(counselors) { counselor ->
            CounselorCard(counselor = counselor) {
                // Pass the counselor's ID as a route parameter
                navController.navigate("counselorchat/${it}")
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CounselorsScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            NavigationSection(
                navController = navController,
                selectedOption = "Counselors",
                onOptionSelected = { /* Handle option selection */ }
            )
        }
    ) {
        Counselors(counselors = CounselorData.counselorList, navController = navController)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CounselorChat(navController: NavHostController) {
    // Get the counselor ID from the navigation arguments
    val counselorId: String? = navController.currentBackStackEntry?.arguments?.getString("counselorId")

    // Use the counselorId to get the specific counselor from your data
    val selectedCounselor: Counselor? = CounselorData.counselorList.find { it.id == counselorId }

    Scaffold(
    ) {
        // Display the counselor chat content
        if (selectedCounselor != null) {
            CounselorChatContent(selectedCounselor = selectedCounselor, navController = navController)
        } else {
            // Handle the case when selectedCounselor is null
            // You may want to show an error message or navigate back
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CounselorChatContent(
    selectedCounselor: Counselor,
    navController: NavHostController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 16.dp) // Add bottom padding for elevation effect
    ) {
        // TopAppBar with a back arrow
        TopAppBar(
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(selectedCounselor.profilePictureResId),
                        contentDescription = "Counselor profile picture",
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = selectedCounselor.name,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .wrapContentSize(Alignment.CenterStart)
                    )
                }
            },
            navigationIcon = {
                // Back arrow icon to navigate back to the Counselors screen
                IconButton(
                    onClick = {
                        navController.popBackStack()
                    }
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 8.dp)
        )

        // Display chat messages using LazyColumn
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            items(selectedCounselor.chatMessages) { message ->
                ChatMessageItem(message = message)
            }
        }

        // Input field for sending new messages
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 8.dp) // Adjust padding
        ) {
            // Image icon
            IconButton(
                onClick = { /* Handle image icon click */ },
                modifier = Modifier
                    .size(40.dp)
                    .padding(4.dp)
            ) {
                val imageIconPainter = painterResource(R.drawable.image)
                Icon(painter = imageIconPainter, contentDescription = "Imaged")
            }

            // BasicTextField with rounded edges
            BasicTextField(
                value = "Type a message...",
                onValueChange = { /* Handle text input change */ },
                textStyle = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .weight(1f)
                    .background(
                        color = MaterialTheme.colorScheme.background,
                        shape = RoundedCornerShape(16.dp) // Add rounded corners
                    )
                    .border(
                        width = 2.dp, // Specify the width of the border
                        color = MaterialTheme.colorScheme.primary, // Specify the color of the border
                        shape = RoundedCornerShape(16.dp) // Match the shape of the background
                    )
                    .padding(8.dp)
            )
            // Audio icon
            IconButton(
                onClick = { /* Handle audio icon click */ },
                modifier = Modifier
                    .size(40.dp)
                    .padding(4.dp)
            ) {
                val audioIconPainter = painterResource(R.drawable.mic)
                Icon(painter = audioIconPainter, contentDescription = "Audio")
            }

            // Send button
            IconButton(
                onClick = { /* Handle send button click */ },
                modifier = Modifier
                    .size(40.dp)
                    .padding(4.dp)
            ) {
                val sendIconPainter = painterResource(R.drawable.send)
                Icon(painter = sendIconPainter, contentDescription = "Send")
            }
        }
    }
}

@Composable
fun ChatMessageItem(message: ChatMessage) {
    // Customize the appearance of each chat message item
    Text(
        text = message.message,
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NewCommunitiesScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            NavigationSection(
                navController = navController,
                selectedOption = "New Communities",
                onOptionSelected = { /* Handle option selection */ }
            )
        }
    ) {
        Communities(CommunityData.communityList, 1f) // Use the existing community list
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavBar(drawerState: DrawerState) {
    val coroutineScope = rememberCoroutineScope() // Create a CoroutineScope

    TopAppBar(
        title = { "Faraja" },
        navigationIcon = {
            IconButton(onClick = {
                coroutineScope.launch {
                    // Toggle the drawer state
                    if (drawerState.isClosed) {
                        drawerState.open() // Open the drawer
                    } else {
                        drawerState.close() // Close the drawer
                    }
                }
            }) {
                Icon(Icons.Filled.Menu, contentDescription = "Menu") // Replace with your desired icon
            }
        }
        // You can add other AppBar configurations if necessary
    )
}




@Composable
fun CommunityCard(community: Community) {
    Column(modifier = Modifier.clickable { /* To Do */ }) {
        Row(
            modifier = Modifier.padding(top = 15.dp, start = 15.dp)
        ) {
            Image(painter = painterResource(R.drawable.guitar), contentDescription = "Community profile picture", modifier = Modifier
                .size(40.dp)
                .clip(
                    CircleShape
                )
                .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(text = community.name,
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(text = "${community.members} members",
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Spacer(modifier = Modifier.width(140.dp))
            ExtendedFloatingActionButton(
                onClick = { /*TODO*/ },
                modifier = Modifier.height(30.dp)
            ) {
                Row {
                    Icon(Icons.Filled.Add, contentDescription = "", modifier = Modifier.size(18.dp))
                    Text(text = "Join", modifier = Modifier.padding(start = 10.dp))
                }
            }
        }
        Text(text = community.description, modifier = Modifier.padding(16.dp, top = 8.dp, end = 25.dp, bottom = 10.dp), style = MaterialTheme.typography.bodySmall)
    }

}

@Composable
fun Communities(communities: List<Community>, opacity: Float) {
    AnimatedVisibility(
        visible = opacity > 0,
        enter = fadeIn() + expandHorizontally(),
        exit = fadeOut() + shrinkHorizontally()
    ) {
        LazyColumn(modifier = Modifier.padding(top = 75.dp)) {
            items(communities) { community ->
                CommunityCard(community = community)
            }
        }
    }
}

@Composable
fun ToggleButton(
    text: String,
    selected: Boolean,
    onToggle: () -> Unit
) {
    Button(
        onClick = {
        onToggle()
    },
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background
        )
    ) {
        Text(text = text, color = if (selected) Color.White else MaterialTheme.colorScheme.primary)

    }
}
