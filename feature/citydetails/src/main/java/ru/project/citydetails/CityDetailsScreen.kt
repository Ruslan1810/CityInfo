package ru.project.citydetails

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.orbitmvi.orbit.compose.collectSideEffect
import ru.project.core.theme.AppTypography
import ru.project.core.theme.TextSecondary
import ru.project.core.ui.ButtonText
import ru.project.core.ui.ErrorScreenContent
import ru.project.domain.model.CityItem
import androidx.core.net.toUri


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityDetailsScreen(
    cityItem: CityItem,
    onBack: () -> Unit,
    viewModel: CityDetailsViewModel = hiltViewModel(),
) {
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()
    val context = LocalContext.current

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is CityDetailsSideEffect.OpenBrowser -> {
                openBrowser(context, sideEffect.url)
            }

            CityDetailsSideEffect.GoBack -> {
                onBack()
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadCity(cityItem)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.city_details_title),
                        style = AppTypography.topAppBarTitle,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.city_details_back),
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(),
            )
        }
    ) { paddingValues ->
        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }

            state.error != null -> {
                ErrorScreenContent(
                    customTitle = state.error!!,
                    customMessage = state.error!!,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    onClick = viewModel::retry
                )
            }

            state.cityItem != null -> {
                CityDetailsContent(
                    cityItem = state.cityItem!!,
                    onSearchInBrowser = viewModel::onSearchInBrowser,
                    modifier = Modifier.padding(paddingValues),
                )
            }
        }
    }
}

@Composable
private fun CityDetailsContent(
    cityItem: CityItem,
    onSearchInBrowser: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            DetailRow(
                label = stringResource(R.string.city_details_name),
                value = cityItem.name
            )
            DetailRow(
                label = stringResource(R.string.city_details_country),
                value = cityItem.country
            )
            DetailRow(
                label = stringResource(R.string.city_details_population),
                value = cityItem.pop.toString()
            )
        }

        ButtonText(stringResource(R.string.city_details_search_button)) {
            onSearchInBrowser()
        }
    }
}

@Composable
private fun DetailRow(
    label: String,
    value: String,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
    ) {
        Text(
            text = label,
            style = AppTypography.labelLarge,
            color = TextSecondary,
        )
        Text(
            text = value,
            style = AppTypography.labelMedium,
            color = TextSecondary,
        )
    }
}

private fun openBrowser(context: Context, url: String) {
    try {
        context.startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
    } catch (e: Exception) {
        Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
    }
}



