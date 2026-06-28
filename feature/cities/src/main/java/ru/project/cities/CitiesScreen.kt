package ru.project.cities

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import org.orbitmvi.orbit.compose.collectSideEffect
import ru.project.cities.model.CitiesSideEffect
import ru.project.core.theme.AppTypography
import ru.project.core.theme.BackgroundLight
import ru.project.core.theme.IconGray
import ru.project.core.theme.TextPrimary
import ru.project.core.theme.TextPlaceholder
import ru.project.core.ui.ErrorScreenContent
import ru.project.core.ui.ShimmerStripe
import ru.project.domain.model.CityItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitiesScreen(
    onCityClick: (CityItem) -> Unit,
) {
    val vm: CitiesViewModel = hiltViewModel()
    val state by vm.container.stateFlow.collectAsStateWithLifecycle()
    val lazyPagingItems = vm.pagingDataFlow.collectAsLazyPagingItems()

    vm.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is CitiesSideEffect.NavigateToCityDetails -> {
                onCityClick(sideEffect.cityItem)
            }
        }
    }

    LaunchedEffect(Unit) {
        vm.loadInitial()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.cities_title),
                        style = AppTypography.topAppBarTitle ,
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors()
            )
        }
    ) { paddingValues ->
        when {
            lazyPagingItems.loadState.refresh is LoadState.Error -> {
                val error = lazyPagingItems.loadState.refresh as LoadState.Error

                ErrorScreenContent(
                    throwable = error.error,
                    modifier = Modifier.padding(paddingValues),
                    onClick = { lazyPagingItems.retry() },
                )
            }

            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    TextField(
                        value = state.query,
                        onValueChange = { newQuery ->
                            vm.onQueryChanged(newQuery)
                        },
                        placeholder = {
                            Text(
                                text = stringResource(R.string.cities_search_hint),
                                style = AppTypography.bodyMedium,
                                color = TextPlaceholder
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp, horizontal = 16.dp)
                            .background(
                                color = BackgroundLight,
                                shape = RoundedCornerShape(16.dp)
                            ),
                        shape = RoundedCornerShape(16.dp),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            errorIndicatorColor = Color.Transparent,
                        ),
                        singleLine = true,
                        trailingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_search),
                                contentDescription = stringResource(R.string.search),
                                tint = TextPrimary,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                        }
                    )

                    Box(modifier = Modifier.fillMaxSize()) {
                        when {
                            lazyPagingItems.loadState.refresh is LoadState.Loading -> {
                                CityListShimmer(count = 10)
                            }

                            lazyPagingItems.itemCount == 0 -> {
                                Text(
                                    text = if (state.query.isEmpty()) {
                                        stringResource(R.string.cities_empty_hint)
                                    } else {
                                        stringResource(R.string.cities_empty_result)
                                    },
                                    modifier = Modifier.align(Alignment.Center),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }

                            else -> {
                                CityList(
                                    lazyPagingItems = lazyPagingItems,
                                    onCityClick = { vm.onCityClicked(it) },
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CityList(
    lazyPagingItems: LazyPagingItems<CityItem>,
    onCityClick: (CityItem) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) {
        items(
            count = lazyPagingItems.itemCount,
            key = { index -> lazyPagingItems[index]?.id ?: 0 }
        ) { index ->
            val cityItem = lazyPagingItems[index]
            cityItem?.let { city ->
                CityItem(
                    cityItem = city,
                    onClick = { onCityClick(city) },
                )
            }
        }

        when (lazyPagingItems.loadState.append) {
            is LoadState.Loading -> {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }

            is LoadState.Error -> {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        CityListShimmer(
                            count = 3,
                            modifier = Modifier.fillMaxWidth(),
                        )
                        Text(
                            text = stringResource(R.string.cities_load_more_error),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 8.dp),
                        )
                        Button(
                            onClick = { lazyPagingItems.retry() },
                            modifier = Modifier.padding(top = 8.dp),
                        ) {
                            Text(stringResource(R.string.retry))
                        }
                    }
                }
            }

            else -> {}
        }
    }
}

@Composable
private fun CityItem(
    cityItem: CityItem,
    onClick: () -> Unit,
) {
    Column(modifier = Modifier.clickable { onClick() }) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_pin),
                contentDescription = stringResource(R.string.search),
                tint = IconGray,
                modifier = Modifier.padding(start = 2.dp, end = 16.dp)
            )

            Text(
                text = cityItem.name + ", ",
                style = AppTypography.labelMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Text(
                text = cityItem.country,
                style = AppTypography.labelMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }

        HorizontalDivider(
            color = MaterialTheme.colorScheme.outlineVariant,
            thickness = 0.5.dp,
            modifier = Modifier.padding(start = 16.dp),
        )
    }
}

@Composable
fun CityListShimmer(
    modifier: Modifier = Modifier,
    count: Int = 10,
    itemHeight: Dp = 30.dp,
    barWidthPercent: Float = 0.6f,
    barHeight: Dp = 100.dp,
    barCornerRadius: Dp = 8.dp,
) {
    Column(
        modifier = modifier,
    ) {
        repeat(count) { index ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_pin),
                    contentDescription = stringResource(R.string.search),
                    tint = Color.Gray,
                    modifier = Modifier.padding(start = 2.dp)
                )

                ShimmerStripe(
                    itemHeight = itemHeight,
                    barWidthPercent = if (index % 2 == 0) barWidthPercent else barWidthPercent * 1.2f,
                    barHeight = barHeight,
                    barCornerRadius = barCornerRadius
                )
            }

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant,
                thickness = 0.5.dp,
                modifier = Modifier.padding(start = 16.dp),
            )
        }
    }
}