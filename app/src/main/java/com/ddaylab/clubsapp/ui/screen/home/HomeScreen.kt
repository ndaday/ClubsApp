package com.ddaylab.clubsapp.ui.screen.home

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ddaylab.clubsapp.R
import com.ddaylab.clubsapp.di.Injection
import com.ddaylab.clubsapp.ui.ViewModelFactory
import com.ddaylab.clubsapp.ui.common.UiState
import com.ddaylab.clubsapp.ui.components.ClubItem
import com.ddaylab.clubsapp.ui.theme.ClubsAppTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository())
    ),
    navigateToDetail: (Long) -> Unit,
) {
    val groupedClub by viewModel.groupedClub.collectAsState()
    val query by viewModel.query

    viewModel.uiState.collectAsState(initial = UiState.Loading).value.let { uiState ->
        when (uiState) {
            is UiState.Loading -> {
                viewModel.getAllClub()
            }
            is UiState.Success -> {
                Box(modifier = modifier) {
                    val scope = rememberCoroutineScope()
                    val listState = rememberLazyListState()
                    val showButton: Boolean by remember {
                        derivedStateOf { listState.firstVisibleItemIndex > 0 }
                    }
                    LazyColumn(
                        state = listState,
                        contentPadding = PaddingValues(bottom = 80.dp)
                    ) {

                        item {
                            SearchBar(
                                query = query,
                                onQueryChange = viewModel::search,
                                modifier = Modifier.background(MaterialTheme.colors.primary)
                            )
                        }
                        groupedClub.forEach { (initial, clubs) ->
                            stickyHeader {
                                CharacterHeader(initial)
                            }
                            items(clubs, key = { it.id }) { data ->
                                ClubItem(
                                    image = data.image,
                                    title = data.title,
                                    modifier = Modifier
                                        .clickable { navigateToDetail(data.id) }
                                        .fillMaxWidth()
                                        .animateItemPlacement(tween(durationMillis = 100))
                                )
                            }
                        }
                    }
                    AnimatedVisibility(
                        visible = showButton,
                        enter = fadeIn() + slideInVertically(),
                        exit = fadeOut() + slideOutVertically(),
                        modifier = Modifier
                            .padding(bottom = 30.dp)
                            .align(Alignment.BottomCenter)
                    ) {
                        ScrollToTopButton(
                            onClick = {
                                scope.launch {
                                    listState.animateScrollToItem(index = 0)
                                }
                            }
                        )
                    }
                }
            }
            is UiState.Error -> {}
        }
    }
}

@Composable
fun CharacterHeader(
    char: Char,
    modifier: Modifier = Modifier
) {
    Surface(
        color = MaterialTheme.colors.primary,
        modifier = modifier
    ) {
        Text(
            text = char.toString(),
            fontWeight = FontWeight.Black,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null
            )
        },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colors.surface,
            disabledIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        ),
        placeholder = {
            Text(stringResource(R.string.search_club))
        },
        trailingIcon = {
            Icon(imageVector = Icons.Default.Clear,
                contentDescription = null,
                modifier = Modifier.clickable { onQueryChange("") })
        },
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
            .heightIn(min = 48.dp)
            .clip(RoundedCornerShape(16.dp))
    )
}

@Composable
fun ScrollToTopButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .shadow(10.dp, shape = CircleShape)
            .clip(shape = CircleShape)
            .size(56.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.White,
            contentColor = MaterialTheme.colors.primary
        )
    ) {
        Icon(
            imageVector = Icons.Filled.KeyboardArrowUp,
            contentDescription = stringResource(R.string.scroll_to_top),
        )
    }
}

@Composable
@Preview(showBackground = true)
fun ClubContentPreview() {
    ClubsAppTheme {

    }
}